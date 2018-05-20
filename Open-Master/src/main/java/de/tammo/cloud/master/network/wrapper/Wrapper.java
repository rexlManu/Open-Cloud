/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.network.wrapper;

import de.tammo.cloud.network.packet.Packet;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class Wrapper {

    @Getter
    private final WrapperMeta wrapperMeta;

    @Setter
    @Getter
    private Channel channel;

    @Setter
    @Getter
    private boolean verified = false;

    @Getter
    private final ConcurrentLinkedQueue<Packet> queue = new ConcurrentLinkedQueue<>();

    public void sendPacket(final Packet packet) {
        if (this.channel == null) {
            this.queue.offer(packet);
        } else {
            this.channel.writeAndFlush(packet);
        }
    }

    public void disconnect() {
        this.channel.close();
        this.channel = null;
        this.queue.clear();
    }

    public final boolean isConnected() {
        return this.channel != null;
    }

}
