/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.bootstrap;

import de.tammo.cloud.core.exceptions.JavaVersionRequiredException;
import de.tammo.cloud.master.Master;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class MasterBootstrap {

    public static void main(final String[] args) throws JavaVersionRequiredException {
        if (Double.parseDouble(System.getProperty("java.class.version")) < 52) {
            throw new JavaVersionRequiredException();
        } else {
            new MasterBootstrap(args);
        }
    }

    private MasterBootstrap(final String[] args) {
        final OptionParser optionParser = new OptionParser();

        optionParser.accepts("debug");
        optionParser.accepts("help");
        optionParser.accepts("version");

        final OptionSet optionSet = optionParser.parse(args);

        new Master().bootstrap(optionSet);
    }

}
