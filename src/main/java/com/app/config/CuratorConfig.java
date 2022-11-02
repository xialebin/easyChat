package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper")
public class CuratorConfig {

    private String host;

    // 重试休眠时间
    private int sleep_time_ms;
    // 最大重试1000次
    private int max_retries;
    //会话超时时间
    private int session_timeout;
    //连接超时时间
    private int connection_timeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSleep_time_ms() {
        return sleep_time_ms;
    }

    public void setSleep_time_ms(int sleep_time_ms) {
        this.sleep_time_ms = sleep_time_ms;
    }

    public int getMax_retries() {
        return max_retries;
    }

    public void setMax_retries(int max_retries) {
        this.max_retries = max_retries;
    }

    public int getSession_timeout() {
        return session_timeout;
    }

    public void setSession_timeout(int session_timeout) {
        this.session_timeout = session_timeout;
    }

    public int getConnection_timeout() {
        return connection_timeout;
    }

    public void setConnection_timeout(int connection_timeout) {
        this.connection_timeout = connection_timeout;
    }


}
