package com.xu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.utils.RedisUtil;
import com.xu.system.mapper.CarouselMapper;
import com.xu.system.mapper.ClassifyMapper;
import com.xu.system.mapper.ProductMapper;
import com.xu.system.pojo.Carousel;
import com.xu.system.pojo.Classify;
import com.xu.system.pojo.Product;
import com.xu.system.service.SystemFirstStartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 项目启动只有在Bean注册完成后一些初始化操作
 * @author AITIAN
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SystemFirstStartServiceImpl implements SystemFirstStartService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CarouselMapper carouselMapper;
    @Resource
    private ClassifyMapper classifyMapper;
    @Resource
    private RedisUtil redisUtil;
    private static RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        SystemFirstStartServiceImpl.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 项目启动只有在Bean注册完成后才会被调用，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        checkRedisConnection();
//        loadingFirstInfo();
    }

    @Override
    public void loadingFirstInfo() {
        Page<Product> page = productMapper.selectPage(new Page<Product>(1, 5), new QueryWrapper<Product>().eq("classid", 1));
        List<Product> productList = page.getRecords();
        int total = (int) page.getTotal();
        List<Carousel> carouselList = (List<Carousel>)carouselMapper.selectList(null);
        List<Classify> allCategory = (List<Classify>) classifyMapper.selectList(null);

        redisUtil.set("homeProducts",productList);
        redisUtil.set("homeProducts:total", total);
        redisUtil.set("carouselList",carouselList);
        redisUtil.set("allCategory",allCategory);
    }

    @Override
    public void checkRedisConnection() {
        try {
            redisConnectionFactory.getConnection().close();
            log.info("Redis 连接成功！！");
        } catch (DataAccessException e) {
            log.info("Redis 连接失败！！");
            throw new RuntimeException(e);
        }
    }
}
