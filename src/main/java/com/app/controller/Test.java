package com.app.controller;

import com.app.common.ZkLockWithCuratorTemplate;
import com.app.util.SpringContextUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test/")
public class Test {

    //剩余库存
    public static int stock = 100;

    @Autowired
    private ZkLockWithCuratorTemplate zkLockWithCuratorTemplate;

    @RequestMapping("/test")
    public String test() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            executorService.execute(new ThreadTest());
        }

        return "success";
    }

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList();

        while (true) {
            list.add("hello");
        }
    }

}



class ThreadTest extends Thread{

    private RedisTemplate<String,Object> redis;

    @Override
    public void run(){
/*
        redis = SpringContextUtil.getBean("redisTemplate");
        String stock = (String) redis.opsForValue().get("lebin");

        if (Strings.isNotBlank(stock) && Integer.valueOf(stock) > 0) {
            int update = Integer.valueOf(stock) - 1;
            redis.opsForValue().set("lebin",String.valueOf(update));
        }*/

        redis = SpringContextUtil.getBean("redisTemplate");
        ZkLockWithCuratorTemplate zkLockWithCuratorTemplate = new ZkLockWithCuratorTemplate();
        try {
            //获得锁
            boolean flag = zkLockWithCuratorTemplate.getLock();

            if (!flag) {
                return;
            }


            //模拟库存扣减
            String stock = (String) redis.opsForValue().get("lebin");

            if (Strings.isNotBlank(stock) && Integer.valueOf(stock) > 0) {
                int update = Integer.valueOf(stock) - 1;
                redis.opsForValue().set("lebin",String.valueOf(update));
            }

            //锁释放
            try {
                zkLockWithCuratorTemplate.unLock();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


