/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.security.user;

import de.tammo.cloud.security.Hashing;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class CloudUserHandler {

    @Setter
    @Getter
    private ArrayList<CloudUser> cloudUsers = new ArrayList<>();

    public final CloudUser findCloudUserByName(final String name) {
        return this.cloudUsers.stream().filter(cloudUser -> cloudUser.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public final boolean login(final String name, final String input) {
        return Hashing.verify(input, this.findCloudUserByName(name).getHash());
    }

}
