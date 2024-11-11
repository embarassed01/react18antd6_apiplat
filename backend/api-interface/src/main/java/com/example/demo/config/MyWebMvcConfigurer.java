package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置
 */
@Configuration 
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override 
    public void addCorsMappings(CorsRegistry registry) {
        // maxAge: 预检请求的缓存时间（秒）
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedHeaders("*")
            .maxAge(3600)
            .allowedMethods("*")
            .allowedOriginPatterns("*");
    }
}
