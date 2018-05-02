/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogLevel {

    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4);

    @Getter
    private int level;

    public final String getName() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }


}
