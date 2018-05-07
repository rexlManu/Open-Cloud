/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper;

import de.tammo.cloud.core.CloudApplication;
import de.tammo.cloud.core.command.CommandHandler;
import de.tammo.cloud.core.document.DocumentHandler;
import de.tammo.cloud.core.logging.LogLevel;
import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.network.NettyClient;
import de.tammo.cloud.network.handler.PacketDecoder;
import de.tammo.cloud.network.handler.PacketEncoder;
import de.tammo.cloud.network.utils.ConnectableAddress;
import de.tammo.cloud.wrapper.config.settings.Settings;
import de.tammo.cloud.wrapper.network.NetworkHandler;
import de.tammo.cloud.wrapper.network.handler.PacketHandler;
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

    private NettyClient nettyClient;

    @Getter
    private NetworkHandler networkHandler = new NetworkHandler();

    private DocumentHandler documentHandler;

    @Setter
    @Getter
    private Settings settings = new Settings();

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) {
        wrapper = this;

        this.setRunning(true);

        this.logger = new Logger("", "Open-Cloud Wrapper", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printHeader("Open-Cloud Wrapper", this.logger);

        this.documentHandler = new DocumentHandler("de.tammo.cloud.wrapper.config");

        this.setupServer();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        final CommandHandler commandHandler = new CommandHandler("de.tammo.cloud.wrapper.commands", this.logger);

        while (this.running) {
            try {
                commandHandler.executeCommand(reader.readLine(), this.logger);
            } catch (IOException e) {
                this.logger.error("Error while reading command!", e);
            }
        }

        this.shutdown();
    }

    private void setupServer() {
        this.registerPackets();

        this.logger.info(this.settings.getMasterPort());

        this.nettyClient = new NettyClient(new ConnectableAddress(this.settings.getMasterHost(), this.settings.getMasterPort())).withSSL().connect(() -> this.logger.info("Connected to Master!"), channel -> channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler()));
    }

    public void shutdown() {
        this.logger.info("Open-Cloud Wrapper is stopping!");

        this.documentHandler.saveFiles();

        this.setRunning(false);

        System.exit(0);
    }

    private void registerPackets() {

    }

}
