package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    
    void validUesrInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);
}
