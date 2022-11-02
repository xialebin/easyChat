package com.app.config.rabbitmq;

import org.springframework.util.StringUtils;

import java.util.Random;

public class RabbitUtil {


    public static String selectExchangeName(String model){


        //随机模式
        if (model.equals(ExchangeModel.RANDOM.getModel())) {

            Random random = new Random(1);
            int randomResult = random.nextInt(3) + 1;

            return "chat.group.exchange"+ randomResult;
        }

        return "dd";
    }

}
