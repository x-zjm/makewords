package com.zjm.makewords.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * @description:
 * @author: zjm
 * @time: 2019/6/29 0029 16:44
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域名，不要使用*，否则不能使用cookie
        config.addAllowedOrigin("http://www.makewords.com");
        // 允许试用cookie
        config.setAllowCredentials(true);
        // 添加允许请求放入方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 允许的头信息
        config.addAllowedHeader("*");
        // 允许的有效时长
        config.setMaxAge(3600L);
        // 添加映射路径，/** 表示拦截一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);
        // 返回新的cors
        return new CorsFilter(configurationSource);
    }
}