import com.app.SpringApplicationMain;
import com.app.common.ZkLockWithCuratorTemplate;
import com.app.util.SpringContextUtil;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringApplicationMain.class})
public class ZookeeperTest {

    public static int stock = 10;

    @Autowired
    public RedisTemplate<String,Object> redisTemplate;

    @Test
    public void test() throws Exception {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            executorService.execute(new ThreadTest());
        }

        TimeUnit.SECONDS.sleep(30);

        System.out.println("success");
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
                redis.opsForValue().increment("daomeidan",1L);
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