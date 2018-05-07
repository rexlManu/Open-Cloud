/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.setup.requests;

import de.tammo.cloud.core.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

public class StringRequest {

    public void request(final Logger logger, final String request, final BufferedReader reader, final Consumer<String> accept) throws IOException {
        logger.info(request);
        accept.accept(reader.readLine());
    }

}