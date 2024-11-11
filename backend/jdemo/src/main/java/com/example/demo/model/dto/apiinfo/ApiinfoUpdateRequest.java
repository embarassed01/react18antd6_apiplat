package com.example.demo.model.dto.apiinfo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApiinfoUpdateRequest implements Serializable {
    private static final long serialVersionUID = 102L;
    
    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
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
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 状态（0-关闭；1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;
}
