/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.masterapi;

import de.tammo.cloud.masterapi.event.EventHandler;
import lombok.Getter;

public class CloudMasterAPI {

    @Getter
    private final EventHandler eventHandler = new EventHandler();

}
