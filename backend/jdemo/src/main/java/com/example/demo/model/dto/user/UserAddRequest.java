package com.example.demo.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户创建请求体
 */
@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 107L;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色：user, admin
     */
    private String userRole;

    /**
     * 用户密码
     */
    private String userPassword;
}
