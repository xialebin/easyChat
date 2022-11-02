package com.app.common.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class Destroy {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @PreDestroy
    public void test(){
        redisTemplate.opsForValue().increment("destroy_test",100);
    }


}
