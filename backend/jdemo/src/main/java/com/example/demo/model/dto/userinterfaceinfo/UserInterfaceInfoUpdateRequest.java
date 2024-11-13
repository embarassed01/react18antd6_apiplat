package com.example.demo.model.dto.userinterfaceinfo;

import java.io.Serializable;

import lombok.Data;

/**
 * 更新请求
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    private static final long serialVersionUID = 22102L;
    
    /**
     * 主键id
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 状态 0-正常；1-禁用
     */
    private Integer status;
}
