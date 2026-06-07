package com.xu.system.service;

/**
 * @author AITIAN
 */
public interface SystemFirstStartService {

    /**
     * 加载参数缓存数据
     */
    public void loadingFirstInfo();

    /**
     * 检测 redis 连接
     */
    public void checkRedisConnection();
}
