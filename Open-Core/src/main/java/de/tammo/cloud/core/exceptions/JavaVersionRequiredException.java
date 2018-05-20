/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.exceptions;

public class JavaVersionRequiredException extends Exception {

    public JavaVersionRequiredException() {
        super("Java 8 is required to start the cloud!");
    }

}
