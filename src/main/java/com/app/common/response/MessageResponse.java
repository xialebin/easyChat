package com.app.common.response;

import com.app.pojo.ChatMessage;
import org.assertj.core.util.Lists;

import java.util.List;

public class MessageResponse {

    private int code;

    private String msg;

    private List<ChatMessage> list;


    private MessageResponse(int code, String msg, List<ChatMessage> list){
        this.setCode(code);
        this.setMsg(msg);
        this.setList(list);
    }


    public static MessageResponse createSuccessMessageResponse(List<ChatMessage> list){
        return new MessageResponse(0,"success",list);
    }

    public static MessageResponse createFailMessageResponse(String msg){
        return  new MessageResponse(1,msg,Lists.emptyList());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ChatMessage> getList() {
        return list;
    }

    public void setList(List<ChatMessage> list) {
        this.list = list;
    }

}
