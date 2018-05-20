/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import de.tammo.cloud.master.Master;
import jline.console.ConsoleReader;

import java.io.IOException;

public class MasterSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Master.getMaster().getNetworkHandler().getWrapperMetas().isEmpty()) {
            logger.info("To create a wrapper use the following command: \"wrapper create <host>\"!");
        }
    }

}
