/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.handler;

import de.tammo.cloud.network.packet.Packet;
import de.tammo.cloud.network.registry.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> output) throws Exception {
        final int id = byteBuf.readInt();
        final Packet packet = PacketRegistry.getPacketById(id, PacketRegistry.PacketDirection.IN);
        if (packet == null)
            new NullPointerException("Could not find packet by id " + id + "!").printStackTrace();
        else {
            packet.read(new ByteBufInputStream(byteBuf));
            output.add(packet);
        }
    }
}
