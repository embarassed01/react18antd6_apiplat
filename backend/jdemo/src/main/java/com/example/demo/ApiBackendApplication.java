package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

/**
 * springboot app run main entry
 * @EnableScheduling 在springboot中开启对 定时任务 的支持
 */
@EnableScheduling
@SpringBootApplication
@EnableDubbo
@MapperScan("com.example.demo.mapper")
public class ApiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBackendApplication.class, args);
	}

}
