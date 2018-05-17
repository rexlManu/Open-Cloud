/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.security.user;

import lombok.Data;

import java.util.UUID;

@Data
class CloudUser {

    private final String name;

    private final UUID uuid;

    private final String hash;

}
