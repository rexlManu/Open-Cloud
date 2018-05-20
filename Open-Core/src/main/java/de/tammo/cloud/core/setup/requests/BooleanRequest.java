/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.setup.requests;

import de.tammo.cloud.core.logging.Logger;
import jline.console.ConsoleReader;

import java.io.IOException;

public class BooleanRequest {

    public void request(final Logger logger, final String request, final ConsoleReader reader, final Runnable runnable) throws IOException {
        logger.info(request + " Y/N");
        if (reader.readLine().equalsIgnoreCase("y")) {
            runnable.run();
        }
    }

}
