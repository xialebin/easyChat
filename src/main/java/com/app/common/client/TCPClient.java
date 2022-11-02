package com.app.common.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.tomcat.util.buf.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

public class TCPClient {

    private EventLoopGroup eventLoopGroup;


    public TCPClient() throws InterruptedException {
        eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(new TCPClientHandler());
            }
        });

        ChannelFuture f = bootstrap.connect("127.0.0.1",8088).sync();
        String sendMessage = "i am a client";

        //什么是ByteBuf
        ByteBuf out = f.channel().alloc().buffer();
        out.writeBytes(sendMessage.getBytes());

        //new TcpClientConnectPoolFactory();

        f.channel().writeAndFlush(out).sync();
        f.channel().closeFuture().sync();
    }


}
