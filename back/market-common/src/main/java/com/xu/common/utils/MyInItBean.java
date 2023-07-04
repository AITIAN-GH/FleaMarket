package com.xu.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * 一些自定义操作
 * @author AITIAN
 */
@Slf4j
@Component
public class MyInItBean {

    private static RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        MyInItBean.redisConnectionFactory = redisConnectionFactory;
    }

    public static void init(){
        System.out.println("容器初始化完毕！！");
    }

    public static void beforeCleanUp() {
        try {
            log.info("Bean未销毁前");
            redisConnectionFactory.getConnection().flushAll();
        } catch (Exception e) {
            log.info("容器销毁前，redis 清空缓存失败！！");
            throw new RuntimeException(e);
        }

    }

    public static void cleanup(){
        log.info("容器销毁!!");
    }
}
