/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import de.tammo.cloud.core.setup.requests.StringRequest;
import de.tammo.cloud.wrapper.Wrapper;
import jline.console.ConsoleReader;

import java.io.IOException;

public class WrapperSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Wrapper.getWrapper().getConfiguration().getKey().isEmpty()) {
            new StringRequest().request(logger, "Please enter the Key, you recieved from the Open-Master", reader, key -> Wrapper.getWrapper().getConfiguration().setKey(key));
        }
     }

}
