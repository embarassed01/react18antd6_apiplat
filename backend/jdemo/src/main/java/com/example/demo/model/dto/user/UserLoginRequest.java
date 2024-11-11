package com.example.demo.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 108L;

    private String userAccount;

    private String userPassword;
    
}
