/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core;

import de.tammo.cloud.core.logging.Logger;
import joptsimple.OptionSet;

public interface CloudApplication {

    void bootstrap(final OptionSet optionSet);

    void shutdown();

    default void printHeader(final String module, final Logger logger) {
        logger.info("   ____                      _____ _                 _       ");
        logger.info("  / __ \\                    / ____| |               | |     ");
        logger.info(" | |  | |_ __   ___ _ __   | |    | | ___  _   _  __| |      ");
        logger.info(" | |  | | '_ \\ / _ \\ '_ \\  | |    | |/ _ \\| | | |/ _` |  ");
        logger.info(" | |__| | |_) |  __/ | | | | |____| | (_) | |_| | (_| |      ");
        logger.info("  \\____/| .__/ \\___|_| |_|  \\_____|_|\\___/ \\__,_|\\__,_|");
        logger.info("        | |                                                  ");
        logger.info("        |_|                                                   ");

        this.sleep(200);

        logger.info("");

        this.sleep(200);

        logger.info("Starting " + module + "!");
    }

    default void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
