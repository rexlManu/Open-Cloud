/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.network.wrapper;

import lombok.Data;

import java.util.UUID;

@Data
public class WrapperMeta {

    private final UUID uuid;

    private final String host;

    private final String key;
}
