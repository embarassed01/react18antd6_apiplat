package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.apiclientsdk.client.ApiClient;
import com.example.apiclientsdk.model.User;

import jakarta.annotation.Resource;

@SpringBootTest
public class ApiInterfaceApplicationTests {
    
    @Resource
    private ApiClient apiClient;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("victory");
        String usernameByPost = apiClient.getUsernameByPost(user);
        System.out.println(usernameByPost);
    }
}
