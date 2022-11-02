package com.app;

import com.app.config.rabbitmq.RabbitMqServerInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    RabbitMqServerInit rabbitMqServerInit;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //启动绑定队列交换机
        //rabbitMqServerInit.init();
    }
}
