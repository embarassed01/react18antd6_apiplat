package com.example.demo.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 111L;

    private Long id;

    private String userName;

    private String userAccount;

    private String userAvatar;

    private Integer gender;

    private String userRole;

    private Date createTime;

    private Date updateTime;
}
