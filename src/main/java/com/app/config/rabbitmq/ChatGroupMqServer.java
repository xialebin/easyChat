package com.app.config.rabbitmq;

import com.app.common.response.ChatGroupResponse;
import com.app.common.server.group.ChatGroupHandler;
import com.app.util.JsonUtil;
import com.rabbitmq.client.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;
import java.util.Map;


@Component
public class ChatGroupMqServer {

    @RabbitListener(
            bindings  = @QueueBinding(
                            value = @Queue(value = "chat.group.${server.id}.1",durable = "true"),
                            exchange = @Exchange(value = "chat.group.exchange1",type = "fanout")
            ),
           containerFactory = "customContainerFactory1"
    )
    public void AReceiver(@Payload String data, @Headers Map<String,Object> headers, Channel channel){
        this.sendMessage(data,headers,channel);
    }

    @RabbitListener(
            bindings  = {
                    @QueueBinding(
                            value = @Queue(value = "chat.group.${server.id}.2",durable = "true"),
                            exchange = @Exchange(value = "chat.group.exchange2",type = "fanout")
                    )
            },
            containerFactory = "customContainerFactory2"
    )
    public void BReceiver(@Payload String data, @Headers Map<String,Object> headers, Channel channel){
        this.sendMessage(data,headers,channel);
    }

    @RabbitListener(
            bindings  = @QueueBinding(
                            value = @Queue(value = "chat.group.${server.id}.3",durable = "true"),
                            exchange = @Exchange(value = "chat.group.exchange3",type = "fanout")
                    ),
            containerFactory = "customContainerFactory3"
    )
    public void CReceiver(@Payload String data, @Headers Map<String,Object> headers, Channel channel){
        this.sendMessage(data,headers,channel);
    }


    private void sendMessage(String data, Map<String,Object> headers, Channel channel){

        ChatGroupResponse chatGroupResponse = JsonUtil.jsonToBean(data,ChatGroupResponse.class);

        String roomId = chatGroupResponse.getRoomId();

        List<io.netty.channel.Channel> channelList = ChatGroupHandler.chatGroupChannel.get(roomId);

        //执行广播操作
        for (io.netty.channel.Channel channel1 : channelList) {
            channel1.writeAndFlush(new TextWebSocketFrame(JsonUtil.beanToJson(chatGroupResponse)));
        }

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        try {
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
