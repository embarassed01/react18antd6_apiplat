package com.example.demo.model.dto.user;

import lombok.Data;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 用户更新请求体
 */
@Data
public class UserUpdateRequest implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 109L;

    private Long id;

    private String userName;

    private String userAccount;
    
    private String userAvatar;

    private Integer gender;

    private String userRole;

    private String userPassword;
}
