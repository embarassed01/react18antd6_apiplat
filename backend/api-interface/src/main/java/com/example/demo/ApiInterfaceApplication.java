package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * springboot app run main entry
 * @EnableScheduling 在springboot中开启对 定时任务 的支持
 */
@EnableScheduling
@SpringBootApplication
public class ApiInterfaceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiInterfaceApplication.class, args);
	}

}
