package com.example.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis Plus配置
 */
@Configuration 
@MapperScan("com.example.demo.mapper")
public class MybatisPlusConfig {
    
    /**
     * 拦截器配置
     * 
     * 新的分页插件，一缓和二缓 遵循 mybatis的规则，
     * 指定数据库拦截器类型 mysql
     * @return
     */
    @Bean 
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
