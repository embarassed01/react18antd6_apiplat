package com.example.demo.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 接口信息表
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 105L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户账号
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
     * 用户角色(user/admin)
     */
    private String userRole;

    /**
     * 用户密码
     */
    private String userPassword;  
      

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;
}
