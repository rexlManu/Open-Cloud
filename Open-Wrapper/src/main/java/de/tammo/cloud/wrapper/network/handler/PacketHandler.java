/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.network.handler;

import de.tammo.cloud.network.packet.Packet;
import de.tammo.cloud.wrapper.Wrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    public void channelActive(final ChannelHandlerContext ctx) {
        if (Wrapper.getWrapper().getNetworkHandler().getHostFromChannel(ctx.channel()).equals("127.0.0.1")) {
            Wrapper.getWrapper().getNetworkHandler().setMasterChannel(ctx.channel());
            while (!Wrapper.getWrapper().getNetworkHandler().getQueue().isEmpty()) {
                Wrapper.getWrapper().getNetworkHandler().sendPacketToMaster(Wrapper.getWrapper().getNetworkHandler().getQueue().poll());
            }
        }
    }

    protected void channelRead0(final ChannelHandlerContext ctx, final Packet packet) {
        final Packet response = packet.handle(ctx.channel());
        if(response != null) ctx.channel().writeAndFlush(response);
    }

    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        Wrapper.getWrapper().getLogger().info("Master interrupted connection!");
        Wrapper.getWrapper().setRunning(false);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Wrapper.getWrapper().getLogger().warn("Master does not accepted this connection!");
        super.channelInactive(ctx);
    }
}
