package com.example.api_gateway;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.demo.provider.DemoService;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDubbo
public class ApiGatewayApplication {

	@DubboReference
	private DemoService demoService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
		
		ApiGatewayApplication application = context.getBean(ApiGatewayApplication.class);
		String result = application.doSayHello("victory");
		System.out.println("result: " + result);
	}

	public String doSayHello(String name) {
		return demoService.sayHello(name);
	}

	// @Bean
	// public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	// 	return builder.routes()
	// 		.route("toyupiicu", r -> r.path("/yupiicu").uri("http://yupi.icu"))  // 访问localhost:8088/yupiicu就可以成功跳转过去！！
	// 		.route("tobaidu", r -> r.path("/baidu").uri("http://www.baidu.com"))  // 百度无法访问，应该是百度设置了限制
	// 		.build();
	// }

}
