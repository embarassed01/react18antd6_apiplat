package com.example.demo.common;

import java.io.Serializable;

import lombok.Data;

/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 30L;

    /**
     * 主键id
     */
    private Long id;
}
