package com.example.apicommon.service;

import com.example.apicommon.model.entity.UserInterfaceInfo;

public interface InnerUserInterfaceInfoService {
    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
