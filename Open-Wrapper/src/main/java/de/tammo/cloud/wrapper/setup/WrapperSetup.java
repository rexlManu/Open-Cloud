/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import jline.console.ConsoleReader;

import java.util.UUID;

public class WrapperSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) {

    }

    private String generateWrapperKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
