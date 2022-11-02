package com.app.common.server.group;

import com.app.common.response.ChatGroupResponse;
import com.app.config.rabbitmq.ExchangeModel;
import com.app.config.rabbitmq.RabbitUtil;
import com.app.util.JsonUtil;
import com.app.util.SpringContextUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ChatGroupHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用户id => channel示例
    private static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    //聊天室数据
    public static ConcurrentHashMap<String, List<Channel>> chatGroupChannel = new ConcurrentHashMap<>();

    //日志
    private Logger logger = LoggerFactory.getLogger(ChatGroupHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //实现拦截器，前置操作验证用户身份
        if (msg != null && msg instanceof FullHttpRequest) {

            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            //String origin = request.headers().get("Origin");
            //orgin在jmeter测试时候为空

            try{

//                if (StringUtils.isEmpty(uri) || !uri.contains("/ws") || !uri.contains("?")) {
//                    throw new Exception("链接非法");
//                }
//
                String[] uriArray = uri.split("\\?");
                String[] paramsArray = uriArray[1].split("&");
//
//                if (Arrays.isNullOrEmpty(uriArray) || Arrays.isNullOrEmpty(paramsArray)) {
//                    throw new Exception("参数非法");
//                }

                RedisTemplate redis  = SpringContextUtil.getBean("redisTemplate");

                int paramCount = 0;
//                String userId = StringUtil.EMPTY_STRING;

                String userId = String.valueOf(redis.opsForValue().increment("chatGroupUser",1L));
                String roomId = StringUtil.EMPTY_STRING;
                for (String item : paramsArray) {

                    String[] param = item.split("=");

                    switch (param[0]){
                        case "token" :
//                            //验证TOKEN
//                            userId = (String) redis.opsForValue().get(param[1]);
//
//                            if (Strings.isNullOrEmpty(userId)) {
//                                throw new Exception("无此用户");
//                            }
//
//                            paramCount ++;
                            break;
                        case "roomId":
                            //todo 验证聊天室ID状态
                            roomId = param[1];
                            paramCount ++;
                            break;
                        default:break;
                    }
                }

                if (paramCount != 2) {
                    //throw new Exception("参数错误");
                }

                //查看用户是否已经在线
                boolean isOnline = this.isOnline(userId);
                if (isOnline) {
                    //下线处理
                    Channel channel = this.getChannelByUserId(userId);
                    //拿到channel之后先将老连接从内存中清除，如果等回调close清除时可能会顺序上错乱，导致清除的是新用户
                    this.notOnline(userId);
                    channel.close();
                }

                //连接成功
                this.online(ctx.channel(),userId,roomId);

                //重置请求地址
                request.setUri("/ws");

            }catch (Exception e){
                e.printStackTrace();
                ctx.close();
                logger.error("uri is :"+uri+" error is :"+e.getMessage()+"line is :"+e.getStackTrace());
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {


        String data = textWebSocketFrame.text();
        //ChatGroupResponse chatGroupResponse = JsonUtil.jsonToBean(data, ChatGroupResponse.class);

        AmqpTemplate amqpTemplate = SpringContextUtil.getBean("rabbitTemplate");

        String exchange = RabbitUtil.selectExchangeName(ExchangeModel.RANDOM.getModel());
        amqpTemplate.convertAndSend(exchange,"",data);

        //amqpTemplate.convertAndSend("chat_message_exchange_1","",str);

        //获取客户端传输过来的消息
        //String jsonContent = textWebSocketFrame.text();


        //随机给三个交换机投递消息
        //不能完全是随机，要保证相同的roomId投递到同一个交换机
    }


//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("连接捕获异常:"+cause.getMessage(),cause);
//        super.exceptionCaught(ctx, cause);
//    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.notOnline(ctx.channel());

        //当触发handlerRemove channelGroup会自动移除对应客户端的channel
        //clients.remove(ctx.channel());
    }


    private boolean hasUser(Channel channel){

        AttributeKey<String> key = AttributeKey.valueOf("user");
        return (channel.hasAttr(key) || channel.attr(key).get() != null);
    }

    private String getUserByChannel(Channel channel){
        AttributeKey<String> key = AttributeKey.valueOf("user");

        if (this.hasUser(channel)) {
            return channel.attr(key).get();
        }
        return null;
    }

    private String getRoomIdByChannel(Channel channel){

        if (!channel.hasAttr(AttributeKey.valueOf("roomId"))) {
            return null;
        }

        return (String) channel.attr(AttributeKey.valueOf("roomId")).get();
    }

    //下线操作
    private void notOnline(String userId){
        channelMap.remove(userId);
    }

    private void notOnline(Channel channel){

        String userId = this.getUserByChannel(channel);

        if (!StringUtils.isEmpty(userId)) {

            Channel channel1 = channelMap.get(userId);

            //判断要下线的channel与现在的channel是否是同一个
            if (channel1.id().asLongText().equals(channel.id().asLongText())) {
                channelMap.remove(userId);
            }
        }

        String roomId = this.getRoomIdByChannel(channel);

        if (roomId != null) {
            List<Channel> list = chatGroupChannel.get(roomId);
            list.remove(channel);
            chatGroupChannel.put(roomId,list);
        }
    }

    //上线操作
    private void online(Channel channel, String userId, String roomId){
        //先判断用户是否在web系统中登录?
        //这部分代码个人实现,参考上面redis中的验证

        List<Channel> list = chatGroupChannel.get(roomId);

        list = list == null ? Lists.newArrayList() : list;
        list.add(channel);

        //维护聊天室数据
        chatGroupChannel.put(roomId,list);

//        AttributeKey<String> userKey = AttributeKey.valueOf("user");
//        AttributeKey<String> connectKey = AttributeKey.valueOf("connectId");

        channel.attr(AttributeKey.valueOf("user")).set(userId);
        channel.attr(AttributeKey.valueOf("connectId")).set(channel.id().asLongText());
        channel.attr(AttributeKey.valueOf("roomId")).set(roomId);

        channelMap.put(userId, channel);
    }

    //根据用户ID获取用户channel
    private Channel getChannelByUserId(String userId) {
        return channelMap.get(userId);
    }

    //判断用户是否在线
    private Boolean isOnline(String userId) {
        return channelMap.containsKey(userId) && channelMap.get(userId) != null;
    }




}
