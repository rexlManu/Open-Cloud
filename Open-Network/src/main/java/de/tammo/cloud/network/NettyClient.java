/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.tammo.cloud.core.threading.ThreadBuilder;
import de.tammo.cloud.network.utils.ConnectableAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyClient {

    private final ConnectableAddress connectableAddress;

    private EventLoopGroup workerGroup;

    private SslContext sslContext;

    private final boolean EPOLL = Epoll.isAvailable();

    public final NettyClient connect(final Runnable ready, final Consumer<Channel> init) {
        new ThreadBuilder("Netty Client", () -> {
            this.workerGroup = this.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            try {
                final ChannelFuture future = new Bootstrap()
                        .group(this.workerGroup)
                        .channel(this.EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                        .handler(new ChannelInitializer<Channel>() {

                            protected void initChannel(final Channel channel) {
                                if (this.toString() != null)
                                    channel.pipeline().addLast(sslContext.newHandler(channel.alloc(), connectableAddress.getHost(), connectableAddress.getPort()));
                                init.accept(channel);
                            }

                        }).connect(new InetSocketAddress(this.connectableAddress.getHost(), this.connectableAddress.getPort())).syncUninterruptibly();

                ready.run();

                future.channel().closeFuture().syncUninterruptibly();
            } finally {
                this.workerGroup.shutdownGracefully();
            }
        }).setDaemon().start();
        return this;
    }

    public final NettyClient withSSL() {
        try {
            this.sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void disconnect(final Runnable disconnected) {
        this.workerGroup.shutdownGracefully();
        disconnected.run();
    }

}
