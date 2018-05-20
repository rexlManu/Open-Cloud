/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network;

import de.tammo.cloud.core.threading.ThreadBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.RequiredArgsConstructor;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyServer {

    final int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private SslContext sslContext;

    private ChannelFuture future;

    private final boolean EPOLL = Epoll.isAvailable();

    public final NettyServer bind(final Runnable ready, final Consumer<Channel> init) {
        new ThreadBuilder("Netty-Client", () -> {
            this.bossGroup = this.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            this.workerGroup = this.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            try {
                final ChannelFuture future = new ServerBootstrap()
                        .group(this.bossGroup, this.workerGroup)
                        .channel(this.EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<Channel>() {

                            protected void initChannel(final Channel channel) {
                                if (sslContext != null)
                                    channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
                                init.accept(channel);
                            }

                        })
                        .bind(this.port)
                        .syncUninterruptibly();

                ready.run();

                NettyServer.this.future = future;

                future.channel().closeFuture().syncUninterruptibly();
            } finally {
                this.workerGroup.shutdownGracefully();
                this.bossGroup.shutdownGracefully();
            }
        }).setDaemon().start();
        return this;
    }

    public final NettyServer withSSL() {
        try {
            final SelfSignedCertificate certificate = new SelfSignedCertificate();
            this.sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
            certificate.delete();
        } catch (CertificateException | SSLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close(final Runnable closed) {
        if (this.future.channel().isOpen()) {
            this.future.channel().close().syncUninterruptibly();
        }
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
        closed.run();
    }

}
