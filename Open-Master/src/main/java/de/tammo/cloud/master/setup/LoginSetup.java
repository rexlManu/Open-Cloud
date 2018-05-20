/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import de.tammo.cloud.core.setup.requests.StringRequest;
import de.tammo.cloud.master.Master;
import de.tammo.cloud.security.Hashing;
import de.tammo.cloud.security.user.CloudUser;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.UUID;

public class LoginSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Master.getMaster().getCloudUserHandler().getCloudUsers().isEmpty()) {
            logger.info("Currently no user created. Create first user ->");
            new StringRequest().request(logger, "Type in the name for the first user:", reader, name -> {
                if (name.equalsIgnoreCase("exit")) {
                    logger.info("The should not be exit!");
                    Master.getMaster().shutdown();
                } else {
                    try {
                        new StringRequest().request(logger, "Type in the password for the first user:", reader, input -> Master.getMaster().getCloudUserHandler().getCloudUsers().add(new CloudUser(name, UUID.randomUUID(), Hashing.hash(input))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            this.login(logger, reader);
        }
    }

    private void login(final Logger logger, final ConsoleReader reader) throws IOException {
        logger.info("Welcome to the login!");
        new StringRequest().request(logger, "Type in your username:", reader, name -> {
            if (name.equalsIgnoreCase("exit")) {
                Master.getMaster().shutdown();
            } else {
                final CloudUser cloudUser = Master.getMaster().getCloudUserHandler().findCloudUserByName(name);
                if (cloudUser == null) {
                    try {
                        this.login(logger, reader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        new StringRequest().request(logger,"Password:", reader, password -> {
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
            }
        });
    }


}
