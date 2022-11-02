package com.app.common;

import com.app.util.PropertiesUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CuratorBuild {

    public static CuratorFramework build(){
          String host = PropertiesUtil.getProperty("zookeeper.host");
          Integer connection_timeout = Integer.valueOf(PropertiesUtil.getProperty("zookeeper.connection_timeout"));
          //Integer session_timeout = Integer.valueOf(PropertiesUtil.getProperty("zookeeper.session_timeout"));
          Integer sleep_time_ms = Integer.valueOf(PropertiesUtil.getProperty("zookeeper.sleep_time_ms"));
          Integer max_retries = Integer.valueOf(PropertiesUtil.getProperty("zookeeper.max_retries"));

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(host)
                .connectionTimeoutMs(connection_timeout)
//                .sessionTimeoutMs(session_timeout)
                .retryPolicy(new ExponentialBackoffRetry(sleep_time_ms, max_retries))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }


    public void haha(){

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,1,2, TimeUnit.HOURS, new LinkedBlockingDeque(1),new ThreadPoolExecutor.DiscardOldestPolicy());



    }

}
