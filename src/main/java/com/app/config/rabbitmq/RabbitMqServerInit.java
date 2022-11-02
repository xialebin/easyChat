package com.app.config.rabbitmq;

import com.app.common.CuratorBuild;
import com.app.util.SpringContextUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqServerInit {

    private final static String PREFIX = "chat.message.";
    private final static String ONLINE_PATH = "/mq_on_line";
    private final static String LEADER_PATH = "/mq/leader";

    private static volatile boolean isLeader = false;

    private Logger logger = LoggerFactory.getLogger(RabbitMqServerInit.class);
    private CuratorFramework client;


    RabbitMqServerInit(){
        client = CuratorBuild.build();
    }


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private Environment environment;

    //监听选举 判断是否是Master节点
    private void listenIsLeader(){

        LeaderSelector leaderSelector = new LeaderSelector(client, LEADER_PATH, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                //选举成功
                isLeader = true;
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    //创建路径并监听路径节点变动
    private void createAndListen(){

        try {
            //检查节点
            Stat stat = client.checkExists().forPath(ONLINE_PATH);

            if (stat == null) {
                //创建持久节点
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ONLINE_PATH);
            }

            //创建一个临时节点
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(ONLINE_PATH+"/online");
            //String statPath = client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(ONLINE_PATH+"/online");
            //String host = new String(client.getData().forPath(statPath));
            //String useHost = host.replace(".","_");

            this.registerAndBindQueue();


            //创建路径的监听事件
            PathChildrenCache pathChildrenCache = new PathChildrenCache(client,ONLINE_PATH,true);
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            pathChildrenCache.getListenable().addListener((cf,event) -> {

                String dataName1 = new String(event.getData().getData());
                String dataName = dataName1.replace(".","_");

                PathChildrenCacheEvent.Type eventType = event.getType();
                switch (eventType) {
                    case CHILD_REMOVED://子节点移除
                        if (isLeader) {
                            //删除队列
                            System.out.println("删除队列");
                        }
                        break;
                    case CHILD_ADDED:
                        //String queueName =
                        //int a = 1;
                        System.out.println("服务上线"+dataName);
                        break;
                        default:break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除队列


    //创建队列并绑定交换机
    private void registerAndBindQueue(){

        String serverId = environment.getProperty("server.id");

        String queueName1 = PREFIX+serverId+".1";
        String queueName2 = PREFIX+serverId+".2";
        String queueName3 = PREFIX+serverId+".3";

        //实例队列
        Queue queue1 = new Queue(queueName1,true);
        Queue queue2 = new Queue(queueName2,true);
        Queue queue3 = new Queue(queueName3,true);

        SpringContextUtil.registerBean(queueName1,queue1);
        SpringContextUtil.registerBean(queueName2,queue2);
        SpringContextUtil.registerBean(queueName3,queue3);


        Exchange exchange1 = ExchangeBuilder.fanoutExchange("exchange1").durable(true).build();
        Exchange exchange2 = ExchangeBuilder.fanoutExchange("exchange2").durable(true).build();
        Exchange exchange3 = ExchangeBuilder.fanoutExchange("exchange3").durable(true).build();


        SpringContextUtil.registerBean("exchange1",exchange1);
        SpringContextUtil.registerBean("exchange2",exchange2);
        SpringContextUtil.registerBean("exchange3",exchange3);


//        FanoutExchange exchange1 = new FanoutExchange("exchange1");
//        FanoutExchange exchange2 = new FanoutExchange("exchange2");
//        FanoutExchange exchange3 = new FanoutExchange("exchange3");

//        SpringContextUtil.registerBean("exchange1",exchange1);
//        SpringContextUtil.registerBean("exchange2",exchange2);
//        SpringContextUtil.registerBean("exchange3",exchange3);

        Binding bind1 = BindingBuilder.bind(queue1).to((FanoutExchange) exchange1);
        Binding bind2 = BindingBuilder.bind(queue1).to((FanoutExchange) exchange2);
        Binding bind3 = BindingBuilder.bind(queue1).to((FanoutExchange) exchange3);
//        Binding bind2 = BindingBuilder.bind(queue2).to(exchange2);
//        Binding bind3 = BindingBuilder.bind(queue3).to(exchange3);

        SpringContextUtil.registerBean("bind1",bind1);
        SpringContextUtil.registerBean("bind2",bind2);
        SpringContextUtil.registerBean("bind3",bind3);
    }

    //上线操作
    private void onLine(){
        new Thread(this::listenIsLeader).start();
        //this.createAndListen();
    }

    //完成队列与交换机的动态绑定
    public void init(){
        this.onLine();
    }
}
