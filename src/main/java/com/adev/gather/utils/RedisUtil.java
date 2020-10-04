package com.adev.gather.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*加锁*/
    public boolean lock(String key,String value,long expireTime){
        if (stringRedisTemplate.opsForValue().setIfAbsent(key,value,expireTime, TimeUnit.MILLISECONDS)){
            return true;
        }
        return false;
    }

    /*解锁*/
    public void unlock(String key,String value){
        String currentValue = (String)stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
            stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
    }
}
