/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.packet;

import de.tammo.cloud.network.packet.impl.SuccessPacket;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;

import java.io.IOException;

public interface Packet {

    default void read(final ByteBufInputStream byteBuf) throws IOException {}

    default void write(final ByteBufOutputStream byteBuf) throws IOException {}

    default Packet handle(final Channel channel) {
        return new SuccessPacket();
    }

}
