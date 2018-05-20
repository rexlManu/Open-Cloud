/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.bootstrap;

import de.tammo.cloud.core.exceptions.JavaVersionRequiredException;
import de.tammo.cloud.wrapper.Wrapper;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;

public class WrapperBootstrap {

    public static void main(final String[] args) throws JavaVersionRequiredException {
        if (Double.parseDouble(System.getProperty("java.class.version")) < 52) {
            throw new JavaVersionRequiredException();
        } else {
            new WrapperBootstrap(args);
        }
    }

    private WrapperBootstrap(final String[] args) {
        final OptionParser optionParser = new OptionParser();

        optionParser.accepts("debug");
        optionParser.accepts("help");
        optionParser.accepts("version");

        final OptionSet optionSet = optionParser.parse(args);

        System.setProperty("jline.WindowsTerminal.directConsole", "false");

        try {
            new Wrapper().bootstrap(optionSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
