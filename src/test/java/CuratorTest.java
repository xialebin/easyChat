import com.app.SpringApplicationMain;
import com.app.common.ZkLockWithCuratorTemplate;
import com.app.common.stock.DistributedStockAllocate;
import com.app.config.CuratorConfig;
import com.app.util.PropertiesUtil;
import com.app.util.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringApplicationMain.class})
public class CuratorTest {

    @Autowired
    private DistributedStockAllocate distributedStockAllocate;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    @Test
    public void test(){


        SpringContextUtil.getBean("redisTemplate");


        //distributedStockAllocate.test();
        redisTemplate.opsForValue().increment("destroy_test",3);

        System.out.println("--------------进入排队--------------");
        
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
