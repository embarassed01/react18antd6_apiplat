package com.example.demo.model.dto.userinterfaceinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import com.example.demo.common.PageRequest;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 22104L;

    /**
     * 主键id
     */
    private Long id;

    private Long userId;

    private Long interfaceInfoId;

    private Integer totalNum;

    private Integer leftNum;

    private Integer status;
}
