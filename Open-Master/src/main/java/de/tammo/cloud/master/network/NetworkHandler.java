/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.network;

import de.tammo.cloud.master.network.wrapper.Wrapper;
import io.netty.channel.Channel;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;

public class NetworkHandler {

    @Getter
    private final ArrayList<Wrapper> wrappers = new ArrayList<>();

    public final Wrapper getWrapperByUUID(final UUID uuid) {
        return this.wrappers.stream().filter(wrapper -> wrapper.getWrapperMeta().getUuid().toString().equals(uuid.toString())).findFirst().orElse(null);
    }

    public final Wrapper getWrapperByHost(final String host) {
        return this.wrappers.stream().filter(wrapper -> wrapper.getWrapperMeta().getHost().equalsIgnoreCase(host)).findFirst().orElse(null);
    }

    public void addWrapper(final Wrapper wrapper) {
        this.wrappers.add(wrapper);
    }

    public void removeWrapper(final Wrapper wrapper) {
        this.wrappers.remove(wrapper);
    }

    public final String getHostFromChannel(final Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

}
