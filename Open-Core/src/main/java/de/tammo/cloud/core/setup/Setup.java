/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.setup;

import de.tammo.cloud.core.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public interface Setup {

    void setup(final Logger logger, final BufferedReader reader) throws IOException;

}
