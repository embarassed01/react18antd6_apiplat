package com.example.apicommon.service;

import com.example.apicommon.model.entity.Apiinfo;

public interface InnerInterfaceInfoService {
    
    /**
     * 从数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数）
     * @param url
     * @param method
     * @return
     */
    Apiinfo getInterfaceInfo(String url, String method);
}
