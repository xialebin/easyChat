package com.app.controller;

import com.app.common.response.MessageResponse;
import com.app.pojo.ChatMessage;
import com.app.service.ChatMessageService;
import com.app.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/message/")
public class MessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private RedisTemplate<String,Object> redis;


    /**
     * 获取聊天记录
     * @param token    token
     * @param pageNum  页码
     * @param pageSize 页数
     * @param time     截止时间
     * @return         结果
     */
    @RequestMapping("/get_message_list")
    public MessageResponse getMessageList(@RequestParam String token, @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "20") Integer pageSize, Integer time){

        Integer userId = null;
        if (redis.opsForValue().get(token) != null) {
            userId = Integer.valueOf((String) redis.opsForValue().get(token));
        }

        if (userId == null) {
           return MessageResponse.createFailMessageResponse("身份已过期");
        }

        long baseTime;
        if (time == null) {
            baseTime = new Date().getTime();
        }else {
            baseTime = DateTimeUtil.turnSecondToMilliSecond(time);
        }

        List<ChatMessage> list = chatMessageService.selectMessageList(userId,pageNum,pageSize,baseTime);

        for (ChatMessage item : list) {
            item.setUserId(null);
            item.setCreateTime((long) DateTimeUtil.turnMilliSecondToSecond(item.getCreateTime()));
        }

        return MessageResponse.createSuccessMessageResponse(list);
    }
}
