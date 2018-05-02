/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper;

import de.tammo.cloud.core.CloudApplication;
import de.tammo.cloud.core.command.CommandHandler;
import de.tammo.cloud.core.logging.LogLevel;
import de.tammo.cloud.core.logging.Logger;
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

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) {
        wrapper = this;

        this.setRunning(true);
        this.logger = new Logger("", "Open-Cloud Wrapper", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printStartup();

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

    public void shutdown() {
        this.logger.info("Open-Cloud Wrapper is stopping!");
        this.setRunning(false);
        System.exit(0);
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
