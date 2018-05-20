/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper;

import de.tammo.cloud.command.CommandHandler;
import de.tammo.cloud.core.CloudApplication;
import de.tammo.cloud.core.document.DocumentHandler;
import de.tammo.cloud.core.logging.LogLevel;
import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.network.NettyClient;
import de.tammo.cloud.network.handler.PacketDecoder;
import de.tammo.cloud.network.handler.PacketEncoder;
import de.tammo.cloud.network.packet.Packet;
import de.tammo.cloud.network.packet.impl.ErrorPacket;
import de.tammo.cloud.network.packet.impl.SuccessPacket;
import de.tammo.cloud.network.registry.PacketRegistry;
import de.tammo.cloud.network.utils.ConnectableAddress;
import de.tammo.cloud.wrapper.config.configuration.Configuration;
import de.tammo.cloud.wrapper.network.NetworkHandler;
import de.tammo.cloud.wrapper.network.handler.PacketHandler;
import de.tammo.cloud.wrapper.network.packets.WrapperKeyOutPacket;
import de.tammo.cloud.wrapper.network.packets.WrapperKeyValidationInPacket;
import de.tammo.cloud.wrapper.setup.WrapperSetup;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Wrapper implements CloudApplication {

    @Getter
    private static Wrapper wrapper;

    @Getter
    private Logger logger;

    @Getter
    private NetworkHandler networkHandler = new NetworkHandler();

    private DocumentHandler documentHandler;

    private NettyClient nettyClient;

    @Setter
    @Getter
    private Configuration configuration = new Configuration();

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) throws IOException {
        wrapper = this;

        this.setRunning(true);

        this.logger = new Logger("", "Open-Cloud Wrapper", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printHeader("Open-Cloud Wrapper", this.logger);

        this.documentHandler = new DocumentHandler("de.tammo.cloud.wrapper.config");

        final ConsoleReader reader = new ConsoleReader();

        new WrapperSetup().setup(this.logger, reader);

        this.setupServer();

        final CommandHandler commandHandler = new CommandHandler("de.tammo.cloud.wrapper.commands", this.logger);

        while (this.running) {
            try {
                commandHandler.executeCommand(reader.readLine(), this.logger);
            } catch (IOException e) {
                this.logger.error("Error while reading command!", e);
            }
        }

        reader.close();

        this.shutdown();
    }

    private void setupServer() {
        this.registerPackets();

        this.nettyClient = new NettyClient(new ConnectableAddress(this.configuration.getMasterHost(), this.configuration.getMasterPort())).withSSL().connect(() -> this.logger.info("Connected to Master!"), () -> {
            this.logger.warn("Master is currently not available!");
            this.shutdown();
        }, channel -> channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler()));
    }

    public void shutdown() {
        this.logger.info("Open-Cloud Wrapper is stopping!");

        this.setRunning(false);

        this.documentHandler.saveFiles();

        this.nettyClient.disconnect(() -> this.logger.info("Wrapper is disconnected!"));

        System.exit(0);
    }

    private void registerPackets() {
        PacketRegistry.PacketDirection.IN.addPacket(0, SuccessPacket.class);
        PacketRegistry.PacketDirection.IN.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.IN.addPacket(201, WrapperKeyValidationInPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(0, SuccessPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(200, WrapperKeyOutPacket.class);
    }

}
