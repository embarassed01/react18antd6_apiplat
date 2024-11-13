package com.example.demo.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加请求
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    private static final long serialVersionUID = 22101L;

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
}
