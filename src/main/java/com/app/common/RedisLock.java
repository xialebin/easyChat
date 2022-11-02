package com.app.common;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RedisLock {

    //key前缀
    private static final String PREFIX = "lock_key_";

    //过期时间
    private static final String EXPIRE_TIME = "300000";//ms

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    //加锁
    public String lock(String key){

        String lockKey = PREFIX+key;

        String luaScript = "local expire = tonumber(ARGV[1]) return redis.call('set',KEYS[1],KEYS[2],'NX','PX',expire)";
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();

        redisScript.setScriptText(luaScript);
        redisScript.setResultType(String.class);

        String uniqueId = getUniqueId();
        List<String> keys = Arrays.asList(lockKey,uniqueId);

        String flag = redisTemplate.execute(redisScript,keys,EXPIRE_TIME);

        if (Strings.isNotBlank(flag)) {
            return uniqueId;
        }

        return null;
    }

    public String getUniqueId(){
        return "123";
    }

    //释放锁
    public void unLock(String key,String val){

        String luaScript = "local data = redis.call('get',KEYS[1]) if KEYS[2]==data then redis.call('del',KEYS[1]) end return true";

        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(Boolean.class);
        defaultRedisScript.setScriptText(luaScript);

        List<String> list = Arrays.asList(PREFIX+key,val);
        redisTemplate.execute(defaultRedisScript,list);
    }
}
