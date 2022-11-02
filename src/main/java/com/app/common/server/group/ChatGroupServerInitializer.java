package com.app.common.server.group;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ChatGroupServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        //通过SocketChannel去获得对应的管道
        ChannelPipeline pipeline = channel.pipeline();

        //websocket 基于 http协议，所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());

        //对httpMessage 进行聚合，聚合成FullHttpRequest或FullHttpResponse
        //几乎在netty中的编程，都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));

        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //自定义Handler
        pipeline.addLast(new ChatGroupHandler());


        //***************** 以上是用于支持http协议 ******************

        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
         * 会处理握手动作：handshaking(close,ping,pong) ping + pong 心跳
         * 对于websocket都是以frames进行传输的，不同数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //以上添加的顺序不能乱
    }
}
