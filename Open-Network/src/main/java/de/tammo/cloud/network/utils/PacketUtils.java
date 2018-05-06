/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.utils;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PacketUtils {

    public static void writeFile(final File file, final ByteBufOutputStream outputStream) throws IOException {
        final byte[] bytes = Files.readAllBytes(file.toPath());
        outputStream.write(bytes, 0, bytes.length);
    }

    public static void readFile(final String path, final ByteBufInputStream inputStream) throws IOException {
        Files.copy(inputStream, new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
