/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.network.packets;

import de.tammo.cloud.network.packet.Packet;
import de.tammo.cloud.network.packet.impl.SuccessPacket;
import de.tammo.cloud.wrapper.Wrapper;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;

import java.io.IOException;

public class WrapperKeyValidationInPacket implements Packet {

    private boolean valid;

    public final Packet handle(final Channel channel) {
        if (this.valid) {
            Wrapper.getWrapper().getLogger().info("Wrapper key is valid!");
            return new SuccessPacket();
        } else {
            Wrapper.getWrapper().getLogger().info("Wrapper key is not valid!");
            Wrapper.getWrapper().shutdown();
            return null;
        }
    }

    public void read(final ByteBufInputStream byteBuf) throws IOException {
        this.valid = byteBuf.readBoolean();
    }
}
