/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.network.packets;

import de.tammo.cloud.network.packet.Packet;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class WrapperKeyOutPacket implements Packet {

    private String key;

    public WrapperKeyOutPacket(final String key) {
        this.key = key;
    }

    public void write(final ByteBufOutputStream byteBuf) throws IOException {
        byteBuf.writeUTF(this.key);
    }
}
