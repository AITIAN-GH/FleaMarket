package com.xu.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author AITIAN
 */
public class MyCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOriginPattern("http://127.0.0.1:*");
        //3) 允许的请求方式
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("UPDATE");
        config.addAllowedMethod("OPTIONS");
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //初始化Cors配置源
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        //2.添加映射路径，我们拦截一切请求
        configSource.registerCorsConfiguration("/**", config);

        //3.返回CorsFilter实例.参数:cors配置源
        return new CorsFilter(configSource);
    }
}
