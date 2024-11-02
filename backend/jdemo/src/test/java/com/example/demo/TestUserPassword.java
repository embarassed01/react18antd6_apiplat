package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import jakarta.annotation.Resource;

@SpringBootTest
public class TestUserPassword {
    
    @Test 
    void testUserPassword() {
        String userPassword = "12345678";
        String salt = "victory";
        String encUserPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        System.out.println(encUserPassword);  // 55345222c4db13b1796ff8992779c24d
    }
}
