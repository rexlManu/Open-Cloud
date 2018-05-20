/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.network.packets;

import de.tammo.cloud.network.packet.Packet;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class WrapperKeyValidationOutPacket implements Packet {

    private boolean valid;

    public WrapperKeyValidationOutPacket(final boolean valid) {
        this.valid = valid;
    }

    public void write(final ByteBufOutputStream byteBuf) throws IOException {
        byteBuf.writeBoolean(this.valid);
    }
}
