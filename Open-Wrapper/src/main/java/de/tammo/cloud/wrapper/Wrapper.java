/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper;

import de.tammo.cloud.core.CloudApplication;
import de.tammo.cloud.core.command.CommandHandler;
import de.tammo.cloud.core.logging.LogLevel;
import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.network.NettyClient;
import de.tammo.cloud.network.handler.PacketDecoder;
import de.tammo.cloud.network.handler.PacketEncoder;
import de.tammo.cloud.network.utils.ConnectableAddress;
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

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) {
        wrapper = this;

        this.setRunning(true);
        this.logger = new Logger("", "Open-Cloud Wrapper", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printStartup();

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

        this.nettyClient = new NettyClient(new ConnectableAddress("127.0.0.1", 1337)).withSSL().connect(() -> this.logger.info("Connected to Master!"), channel -> channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler()));
    }

    public void shutdown() {
        this.logger.info("Open-Cloud Wrapper is stopping!");
        this.setRunning(false);
        System.exit(0);
    }

    private void registerPackets() {

    }

    private void printStartup() {
        this.logger.info("   ____                      _____ _                 _ ");
        this.logger.info("  / __ \\                    / ____| |               | |");
        this.logger.info(" | |  | |_ __   ___ _ __   | |    | | ___  _   _  __| |");
        this.logger.info(" | |  | | '_ \\ / _ \\ '_ \\  | |    | |/ _ \\| | | |/ _` |");
        this.logger.info(" | |__| | |_) |  __/ | | | | |____| | (_) | |_| | (_| |");
        this.logger.info("  \\____/| .__/ \\___|_| |_|  \\_____|_|\\___/ \\__,_|\\__,_|");
        this.logger.info("        | |                                            ");
        this.logger.info("        |_|                                            ");

        this.sleep(200);

        this.logger.info("");

        this.sleep(200);

        this.logger.info("Starting Open-Cloud Wrapper!");
    }

    private void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
