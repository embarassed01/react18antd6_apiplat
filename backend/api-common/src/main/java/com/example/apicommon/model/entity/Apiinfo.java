package com.example.apicommon.model.entity;

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
@TableName(value = "apiinfo")
@Data
public class Apiinfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 100L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口名
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态
     */
    private Integer status;

    /**
     * 接口类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;


    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;
}

