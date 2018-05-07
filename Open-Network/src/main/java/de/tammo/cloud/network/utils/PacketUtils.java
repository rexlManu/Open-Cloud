/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.utils;

import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PacketUtils {

    public static void writeFile(final File file, final ByteBufOutputStream outputStream) throws IOException {
        final byte[] bytes = Files.readAllBytes(file.toPath());
        outputStream.write(bytes, 0, bytes.length);
    }

    public static PacketFile readFile(final ByteBufInputStream inputStream) throws IOException {
        return new PacketFile(ByteStreams.toByteArray(inputStream));
    }

}
