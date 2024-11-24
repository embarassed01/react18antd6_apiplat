package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.apicommon.model.entity.Apiinfo;
import com.example.apicommon.model.entity.User;
import com.example.apicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    
    void validUesrInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}


