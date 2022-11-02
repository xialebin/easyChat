package com.app.common.server.group;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class ChatGroupServer {

    private static class SingleWSServer {
        static final ChatGroupServer instance = new ChatGroupServer();
    }

    public static ChatGroupServer getInstance() {
        return SingleWSServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public ChatGroupServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatGroupServerInitializer());
    }

    public void start() {
        this.future = server.bind(8081);
        System.err.println("netty websocket server 启动完毕...");
    }

}
