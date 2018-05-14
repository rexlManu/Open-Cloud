/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.config.settings;

import lombok.Data;

@Data
public class Configuration {

    private String masterHost = "127.0.0.1";

    private int masterPort = 1337;

}
