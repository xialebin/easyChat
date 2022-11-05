package com.app.service.impl;

import com.app.pojo.ChatMessage;
import com.app.service.ChatMessageService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service("ChatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {


    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private RedisTemplate<String,Object> redis;


    //保存聊天记录
    public void saveMessage(ChatMessage chatMessage) throws Exception {

        if (StringUtils.isEmpty(chatMessage.getUserId()) || StringUtils.isEmpty(chatMessage.getContent())) {
            throw new Exception("ChatMessage param error");
        }

        long id = redis.opsForValue().increment("chat_message_mongo_incr_id",1);
        chatMessage.setId(id);

        if (chatMessage.getCreateTime() == null) {
            long time = (new Date()).getTime();
            chatMessage.setCreateTime(time);
        }

        if (chatMessage.getServerId() == null) {
            chatMessage.setServerId(0);
        }

        if (chatMessage.getServerName() == null) {
            chatMessage.setServerName(StringUtil.EMPTY_STRING);
        }

        mongo.insert(chatMessage);
    }


    //查询聊天记录
    public List<ChatMessage> selectMessageList(Integer userId,Integer pageNum,Integer pageSize,Long baseTime){

        Query query = new Query(Criteria.where("userId").is(userId).and("createTime").lte(baseTime));

        query.with(PageRequest.of((pageNum-1),pageSize));
        query.with(new Sort(Sort.Direction.DESC,"id"));

        return mongo.find(query,ChatMessage.class);
    }


}
