/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master;

import de.tammo.cloud.command.CommandHandler;
import de.tammo.cloud.core.CloudApplication;
import de.tammo.cloud.core.document.DocumentHandler;
import de.tammo.cloud.core.logging.LogLevel;
import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.master.network.NetworkHandler;
import de.tammo.cloud.master.network.handler.PacketHandler;
import de.tammo.cloud.master.network.packets.WrapperKeyInPacket;
import de.tammo.cloud.master.network.packets.WrapperKeyValidationOutPacket;
import de.tammo.cloud.master.network.wrapper.Wrapper;
import de.tammo.cloud.master.setup.LoginSetup;
import de.tammo.cloud.master.setup.MasterSetup;
import de.tammo.cloud.network.NettyServer;
import de.tammo.cloud.network.handler.PacketDecoder;
import de.tammo.cloud.network.handler.PacketEncoder;
import de.tammo.cloud.network.packet.impl.ErrorPacket;
import de.tammo.cloud.network.packet.impl.SuccessPacket;
import de.tammo.cloud.network.registry.PacketRegistry;
import de.tammo.cloud.security.user.CloudUserHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Master implements CloudApplication {

    @Getter
    private static Master master;

    @Getter
    private Logger logger;

    @Getter
    private NetworkHandler networkHandler;

    @Getter
    private CloudUserHandler cloudUserHandler;

    private DocumentHandler documentHandler;

    private NettyServer nettyServer;

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) throws IOException{
        master = this;

        this.setRunning(true);

        this.logger = new Logger("", "Open-Cloud", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printHeader("Open-Cloud", this.logger);

        this.networkHandler = new NetworkHandler();

        this.cloudUserHandler = new CloudUserHandler();

        this.documentHandler = new DocumentHandler("de.tammo.cloud.master.config");

        final ConsoleReader reader = new ConsoleReader(System.in, System.out);
        reader.setHistoryEnabled(false);

        new LoginSetup().setup(this.logger, reader);

        new MasterSetup().setup(this.logger, reader);

        this.setupServer(() -> this.logger.info("Server was successfully bound to port 1337"));

        final CommandHandler commandHandler = new CommandHandler("de.tammo.cloud.master.commands", this.logger);

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

    private void setupServer(final Runnable ready) {
        this.registerPackets();

        this.nettyServer = new NettyServer(1337).withSSL().bind(ready, channel -> {
            final String host = this.networkHandler.getHostFromChannel(channel);
            if (!this.networkHandler.isWhitelisted(host)) {
                channel.close().syncUninterruptibly();
                this.logger.warn("A not whitelisted Wrapper would like to connect to this master!");
                return;
            }

            channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler());
            final Wrapper wrapper = this.networkHandler.getWrapperByHost(host);
            if (wrapper != null) {
                wrapper.setChannel(channel);
                this.logger.info("Wrapper from " + wrapper.getWrapperMeta().getHost() + " connected!");
            }
        });
    }

    public void shutdown() {
        this.logger.info("Open-Cloud is stopping!");

        this.documentHandler.saveFiles();

        this.networkHandler.getWrappers().stream().filter(Wrapper::isConnected).forEach(wrapper -> wrapper.getChannel().close().syncUninterruptibly());

        this.nettyServer.close(() -> logger.info("Netty server was closed!"));

        System.exit(0);
    }

    private void registerPackets() {
        PacketRegistry.PacketDirection.IN.addPacket(0, SuccessPacket.class);
        PacketRegistry.PacketDirection.IN.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.IN.addPacket(200, WrapperKeyInPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(0, SuccessPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(201, WrapperKeyValidationOutPacket.class);
    }

}
