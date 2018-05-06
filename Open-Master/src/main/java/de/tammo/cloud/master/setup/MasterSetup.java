/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import de.tammo.cloud.core.setup.requests.StringRequest;
import de.tammo.cloud.master.Master;
import de.tammo.cloud.master.network.wrapper.Wrapper;
import de.tammo.cloud.master.network.wrapper.WrapperMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class MasterSetup implements Setup {

    public void setup(final Logger logger, final BufferedReader reader) throws IOException {
        if (Master.getMaster().getNetworkHandler().getWrappers().isEmpty()) {
            new StringRequest().request(logger, "Type in the host of the first wrapper:", reader, host -> {
                final WrapperMeta wrapperMeta = new WrapperMeta(UUID.randomUUID(), host);
                final Wrapper wrapper = new Wrapper(wrapperMeta);
                Master.getMaster().getNetworkHandler().addWrapper(wrapper);
                logger.info("Waiting for connection...");
                while (!wrapper.isConnected()) {
                    System.out.flush();
                }
                logger.info("Wrapper is connected!");
            });
        }
    }

}
