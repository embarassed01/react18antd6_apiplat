package com.example.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
@EnableAutoConfiguration
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	// @Bean
	// public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	// 	return builder.routes()
	// 		.route("toyupiicu", r -> r.path("/yupiicu").uri("http://yupi.icu"))  // 访问localhost:8088/yupiicu就可以成功跳转过去！！
	// 		.route("tobaidu", r -> r.path("/baidu").uri("http://www.baidu.com"))  // 百度无法访问，应该是百度设置了限制
	// 		.build();
	// }

}
