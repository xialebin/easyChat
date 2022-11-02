package com.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.nio.channels.Channel;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@MapperScan("com.app.dao")
public class SpringApplicationMain {


    //springboot 启动方法
    public static void main(String[] args) {

        //启动spring应用
        ApplicationContext context = SpringApplication.run(SpringApplicationMain.class, args);
//        ApplicationContext context = SpringApplication.run(SpringApplicationMain.class,args);
//        ZkClient zkClient = context.getBean(ZkClient.class);
//        zkClient.register();
    }

}

