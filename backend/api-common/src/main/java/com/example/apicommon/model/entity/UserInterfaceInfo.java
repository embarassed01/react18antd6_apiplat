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
 * 用户调用接口关系表
 */
@TableName(value = "user_interface_info")
@Data
public class UserInterfaceInfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 20001L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 状态 0-正常；1-禁用 （默认就是正常）
     */
    private Integer status;


    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;
}

