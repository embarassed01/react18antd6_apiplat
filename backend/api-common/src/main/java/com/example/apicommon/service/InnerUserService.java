package com.example.apicommon.service;

import com.example.apicommon.model.entity.User;

public interface InnerUserService {
    
    /**
     * 数据库中查询是否已分配给用户密钥（accessKey）
     * @param accesssKey
     * @return
     */
    User getInvokeUser(String accesssKey);
}
