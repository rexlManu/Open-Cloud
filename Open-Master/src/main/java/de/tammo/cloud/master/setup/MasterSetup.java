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
import de.tammo.cloud.security.Hashing;
import de.tammo.cloud.security.user.CloudUser;
import de.tammo.cloud.security.user.CloudUserHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class MasterSetup implements Setup {

    public void setup(final Logger logger, final BufferedReader reader) throws IOException {
        if (Master.getMaster().getCloudUserHandler().getCloudUsers().isEmpty()) {
            new StringRequest().request(logger, "Type in the name of the first user:", reader, name -> {
                try {
                    new StringRequest().request(logger, "Type in the password for the first user:", reader, input -> {
                        Master.getMaster().getCloudUserHandler().getCloudUsers().add(new CloudUser(name, UUID.randomUUID(), Hashing.hash(input)));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            this.login(logger, reader);
        }

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

    private void login(final Logger logger, final BufferedReader reader) throws IOException {
        new StringRequest().request(logger, "Type in your username:", reader, name -> {
            final CloudUser cloudUser = Master.getMaster().getCloudUserHandler().findCloudUserByName(name);
            if (cloudUser == null) {
                try {
                    this.login(logger, reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new StringRequest().request(logger, "Password:", reader, password -> {
                        if (Hashing.verify(password, cloudUser.getHash())) {
                            logger.info("You are now logged in!");
                        } else {
                            try {
                                this.login(logger, reader);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
