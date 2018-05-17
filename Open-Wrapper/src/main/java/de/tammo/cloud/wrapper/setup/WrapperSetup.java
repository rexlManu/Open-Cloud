/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;

import java.io.BufferedReader;
import java.util.UUID;

public class WrapperSetup implements Setup {

    public void setup(final Logger logger, final BufferedReader reader) {
        logger.info("You have to enter this key in the master!");
        logger.info("Your key is: " + this.generateWrapperKey());
        logger.info("Never show this key other guys!");
    }

    private String generateWrapperKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
