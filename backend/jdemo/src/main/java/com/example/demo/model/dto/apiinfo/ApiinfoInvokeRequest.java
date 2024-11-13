package com.example.demo.model.dto.apiinfo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApiinfoInvokeRequest implements Serializable {
    private static final long serialVersionUID = 1102L;
    
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String requestParams;
}
