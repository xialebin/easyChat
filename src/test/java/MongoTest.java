import com.app.SpringApplicationMain;
import com.app.common.RedisLock;
import com.app.pojo.ChatMessage;
import com.app.service.ChatMessageService;
import com.app.util.DateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringApplicationMain.class})
public class MongoTest {

    @Autowired
    RedisLock redisLock;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private MongoTemplate mongo;

    @Test
    public void mongoInsert(){

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setContent("我们今天真的很开心，你知道吗哈哈哈");
        chatMessage.setType("text");
        chatMessage.setUserId(33189);

        String startTime = DateTimeUtil.getMilliSecond();

        try {

            for (int i = 0; i < 10000; i++) {
                chatMessageService.saveMessage(chatMessage);
            }

        } catch (Exception e) {
            System.out.println("出现错误");
        }

        String endTime = DateTimeUtil.getMilliSecond();

        System.out.println(startTime);
        System.out.println(endTime);
    }

    @Test
    public void mongoSelect(){

        long base = 0L;
        for (int i = 0; i < 100; i++) {
            long startTime = new Date().getTime();
            Query query = new Query(Criteria.where("userId").is(33189).and("createTime").lte(1593873448480L));
            query.with(PageRequest.of(0,20));

            query.with(new Sort(Sort.Direction.DESC,"id"));
            List<ChatMessage> list = mongo.find(query,ChatMessage.class);

            long endTime = new Date().getTime();

            base += (endTime - startTime);
        }

        System.out.println(base);
    }

    @Test
    public void testLua(){
        String result = redisLock.lock("hi");
        System.out.println(result);
        redisLock.unLock("hi",result);
        System.out.println("success");
    }

}
