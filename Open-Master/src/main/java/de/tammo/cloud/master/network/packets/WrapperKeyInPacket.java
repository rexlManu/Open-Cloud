/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.network.packets;

import de.tammo.cloud.master.Master;
import de.tammo.cloud.master.network.wrapper.Wrapper;
import de.tammo.cloud.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;

import java.io.IOException;

public class WrapperKeyInPacket implements Packet {

    private String key;

    public final Packet handle(final Channel channel) {
        final Wrapper wrapper = Master.getMaster().getNetworkHandler().getWrapperByHost(Master.getMaster().getNetworkHandler().getHostFromChannel(channel));
        if (wrapper != null) {
            if (wrapper.getWrapperMeta().getKey().equals(this.key)) {
                //SUCCESS
                wrapper.setVerified(true);
                return null;
            }
        }
        return null;
    }

    public void read(final ByteBufInputStream byteBuf) throws IOException {
        this.key = byteBuf.readUTF();
    }
}
