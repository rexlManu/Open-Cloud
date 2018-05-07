/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PacketFile {

    private final byte[] bytes;

    public PacketFile(final byte[] bytes) {
        this.bytes = bytes;
    }

    public void saveAsFile(final String path) throws IOException {
        Files.write(new File(path).toPath(), this.bytes);
    }
}
