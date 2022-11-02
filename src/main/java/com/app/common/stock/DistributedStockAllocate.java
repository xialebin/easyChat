package com.app.common.stock;


import com.app.common.CuratorBuild;
import com.app.common.stock.pojo.Stock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DistributedStockAllocate {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //线程安全存储商品的库存信息
    public static ConcurrentHashMap<Integer,Stock> stockHashMap;
    public static ConcurrentHashMap<Object,Object> stockNum;

    private static final String PATH = "/on_line";

    private static final int BALANCE_DEC = 2;

    private static final String STOCK_KEY = "goods_stock_init_id";
    private static final String STOCK_TEMP_KEY = "goods_stock_temp_id";

    private CuratorFramework client;

    public DistributedStockAllocate(){
        client = CuratorBuild.build();
    }


    //初始化库存
    public void initStock(){

        Map<Object,Object> stockMap = redisTemplate.opsForHash().entries(STOCK_KEY);
        stockNum.putAll(stockMap);


//        Set<Object> list = redisTemplate.opsForSet().members("goods_stock_init_id");

        CuratorFramework client = CuratorBuild.build();
        Stat stat = new Stat();
        try {
            client.getData().storingStatIn(stat).forPath("/on_line");

            //在线机器数量
            int machineNum = stat.getNumChildren();

            Map<Object,Object> stockMap1 = redisTemplate.opsForHash().entries(STOCK_KEY);


            for (Map.Entry<Object, Object> entry : stockMap.entrySet()) {


            }




        } catch (Exception e) {
            e.printStackTrace();
        }






        /*

        String testPath = "test";

        //创建连接

        try {

            CuratorFramework client = CuratorBuild.build();

//            Stat stat = client.checkExists().forPath("/on_line");
//
//            if (stat != null) {
//                client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/test_bin");
//            }

            //创建一个持久化节点
            //client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/on_line");

            PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/test",true);
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            pathChildrenCache.getListenable().addListener((cf,event) -> {
                PathChildrenCacheEvent.Type eventType = event.getType();
                switch (eventType) {
                    case CONNECTION_RECONNECTED:
                        pathChildrenCache.rebuild();
                        break;
                    case CONNECTION_SUSPENDED:
                        break;
                    case CONNECTION_LOST:
                        System.out.println("connection lost");
                        break;
                    case CHILD_ADDED:
                        System.out.println("Child added");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("Child updated");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("Child removed");
                        break;
                    default:break;
                }
            });

            //创建一个持久化节点
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/test/online");
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/test/online");
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/test/online");
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/test/online");

            System.out.println("创建完成");
            TimeUnit.MINUTES.sleep(5);
            System.out.println("已经关闭");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
*/

        //如果testPath存在，删除路径
        try {
            Stat stat1 = client.checkExists().forPath(PATH);

            if (stat != null) {
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(PATH);
            }
            //创建testPath
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(PATH,"node".getBytes());

            //创建PathChildrenCache true代表缓存数据到本地
            PathChildrenCache pathChildrenCache = new PathChildrenCache(client,PATH,true);
            //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

            pathChildrenCache.getListenable().addListener((cf,event) -> {
                PathChildrenCacheEvent.Type eventType = event.getType();
                //event.getData().getStat();
                switch (eventType) {

                    case CONNECTION_RECONNECTED:
                        pathChildrenCache.rebuild();
                        break;
                    case CONNECTION_SUSPENDED:
                        break;
                    case CONNECTION_LOST:
                        System.out.println("connection lost");
                        break;
                    case CHILD_ADDED:
                        System.out.println("Child added");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("Child updated");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("Child removed");
                        break;
                        default:break;
                }
            });



            //创建子节点1
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/");
            Thread.sleep(1000);
            //创建子节点2
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/");
            Thread.sleep(1000);
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/");
            Thread.sleep(1000);
            //删除子节点1
//            client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/"+testPath+"/1");
//            Thread.sleep(1000);
//            //删除子节点2
//            client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/"+testPath+"/2");
//            Thread.sleep(1000);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public synchronized void checkAndUpdateOnServerChange(Integer goodsId){

        CuratorFramework client = CuratorBuild.build();
        Stat stat = new Stat();

        try {

            //client.getData().storingStatIn(stat).forPath(PATH);

            List<String> childrenPath = client.getChildren().forPath(PATH);

            //现在在线的服务数量
            int childrenNum = childrenPath.size();

            Stock stock = stockHashMap.get(goodsId);

            //重新散列服务
            if (stock.getServerNum() > childrenNum) {

                long allNum = (long) redisTemplate.opsForHash().get(STOCK_KEY,goodsId);

                //allNum - (stock.getServerNum() - childrenNum) *



//                Long amount = stock.getServerNum() * stock.getAmount();
//
//                if (this.isBalance(childrenPath)) {
//
//                }
            }






            client.setData().forPath("/on_line",String.valueOf(childrenNum).getBytes());

//
//            Map<Object,Object> stockMap = redisTemplate.opsForHash().entries("goods_stock_init_id");
//
//            for (Map.Entry<Object, Object> entry : stockMap.entrySet()) {
//
//                int goodsId = (int) entry.getKey();
//
//
//
//            }






        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isBalance(List<String> childrenPath){
        Integer balance = this.getBalanceDec(childrenPath);
        if (balance != null && balance <= BALANCE_DEC) {
            return true;
        }
        return false;
    }


    private Integer getBalanceDec(List<String> childrenPath){

        try {

            int minLevel = 0;
            int maxLevel = 0;

            for (String item : childrenPath) {
                byte[] data = client.getData().forPath(item);
                String level = new String(data);

                Integer levelInt = Integer.valueOf(level);

                if (levelInt < minLevel) {
                    minLevel = levelInt;
                }else if(levelInt > maxLevel) {
                    maxLevel = levelInt;
                }
            }

            return  maxLevel - minLevel;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
