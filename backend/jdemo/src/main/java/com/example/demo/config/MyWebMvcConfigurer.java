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
            // .allowedOrigins("http://localhost:8000","http://localhost:8080");


        // // 设置允许跨域的路径
        // registry.addMapping("/**")
        //     // 设置允许跨域请求的域名
        //     // 当**Credentials为true时，**Origin不能为星号，需要为具体的ip地址【如果接口不带cookie，ip无需设置为具体ip】
        //     .allowedOrigins("http://localhost:8000","http://127.0.0.1:8000")
        //     // 允许发送Cookie
        //     .allowCredentials(true)
        //     // .allowedOriginPatterns("*")  // 放行哪些域名(必须用patterns，否则*会和allowCredentials冲突！！)
        //     // 设置允许的方法
        //     .allowedMethods("*")
        //     // .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
        //     .allowedHeaders("*")
        //     // .exposedHeaders("*");
        //     // 跨域允许时间
        //     .maxAge(3600);
    }
}
