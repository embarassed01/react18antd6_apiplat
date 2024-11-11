package com.example.demo.model.dto.apiinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiinfoAddRequest implements Serializable {
    private static final long serialVersionUID = 101L;

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
     * 请求类型
     */
    private String method;
}
