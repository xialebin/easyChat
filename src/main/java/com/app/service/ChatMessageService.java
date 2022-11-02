package com.app.service;

import com.app.pojo.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatMessage chatMessage) throws Exception;
    List<ChatMessage> selectMessageList(Integer userId, Integer pageNum, Integer pageSize, Long baseTime);
    long delMessage(Long id);
}
