/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.setup;

import de.tammo.cloud.core.logging.Logger;
import jline.console.ConsoleReader;

import java.io.IOException;

public interface Setup {

    void setup(final Logger logger, final ConsoleReader reader) throws IOException;

}
