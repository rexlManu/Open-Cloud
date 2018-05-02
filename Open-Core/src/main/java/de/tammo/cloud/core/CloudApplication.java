/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core;

import joptsimple.OptionSet;

public interface CloudApplication {

    void bootstrap(final OptionSet optionSet);

    void shutdown();

}
