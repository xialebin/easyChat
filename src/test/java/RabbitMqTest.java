import com.app.SpringApplicationMain;
import com.app.util.SpringContextUtil;
import com.rabbitmq.client.AMQP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringApplicationMain.class})
public class RabbitMqTest {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test(){

        for (int i = 0; i < 8; i++) {
            new Thread(()->{
                while (true) {
                    redisTemplate.opsForValue().increment("nihaobin",1);
                }
            }).start();
        }


        try {

            System.out.println("start");
            TimeUnit.SECONDS.sleep(20);

            String s_start = (String) redisTemplate.opsForValue().get("nihaobin");

            TimeUnit.SECONDS.sleep(50);

            String s_end = (String) redisTemplate.opsForValue().get("nihaobin");

            System.out.println((Integer.valueOf(s_end) - Integer.valueOf(s_start)));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");





//        redisTemplate.opsForValue().increment("nihaobin",1L);
//
//        System.out.println("------start-----");
//        String str = "hello world exchange";
//        amqpTemplate.convertAndSend("chat.message.exchange1","",str);
//        amqpTemplate.convertAndSend("chat.message.exchange2","",str);
//        amqpTemplate.convertAndSend("chat.message.exchange3","",str);
    }


    @Test
    public void test1(){

        amqpTemplate.convertAndSend("chat.message.exchange2","","women2");
        amqpTemplate.convertAndSend("chat.message.exchange1","","women1");
        amqpTemplate.convertAndSend("chat.message.exchange3","","women3");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
