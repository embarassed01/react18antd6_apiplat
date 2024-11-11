package com.example.apiclientsdk;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.example.apiclientsdk.client.ApiClient;

import lombok.Data;

@AutoConfiguration
@ConfigurationProperties("api.client")  // 给所有的配置加一个前缀: api.client
@Data
@ComponentScan  // 扫包注解
public class ApiClientConfig {
    
    private String accessKey;

    private String secretKey;

    @Bean
    public ApiClient ApiClient() {
        return new ApiClient(accessKey, secretKey);
    }
}
