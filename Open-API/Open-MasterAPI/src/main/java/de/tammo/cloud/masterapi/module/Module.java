/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.masterapi.module;

public interface Module {

    default void onLoad() {}

    default void onEnable() {}

    default void onDisable() {}

    default void onStop() {}

    default String getName() {
        return "";
    }

    default String getVersion() {
        return "1.0-SNAPSHOT";
    }

    default String getAuthor() {
        return "";
    }

}
