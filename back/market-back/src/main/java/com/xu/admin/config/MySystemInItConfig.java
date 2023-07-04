package com.xu.admin.config;

import com.xu.common.utils.MyInItBean;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;

/**
 * 用于在项目启动后执行一些初始化操作
 * @author AITIAN
 */
@Configuration
public class MySystemInItConfig {

    /**
     * ApplicationRunner接口，用于在项目启动后执行一些初始化操作
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            // 执行初始化操作
            MyInItBean.init();
        };
    }

    /**
     * EventListener注解只有在Bean未销毁前才能被调用
     */
    @EventListener(ContextClosedEvent.class)
    public void handleContextClosedEvent() {
        // 执行清理操作
        MyInItBean.beforeCleanUp();
    }

    /**
     * PreDestroy注解 执行在Bean销毁后
     */
    @PreDestroy
    public void cleanup() {
        // 执行清理操作
        MyInItBean.cleanup();
    }
}
