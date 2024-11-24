package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.apicommon.model.entity.Apiinfo;

public interface ApiinfoService extends IService<Apiinfo> {
    
    /**
     * 校验
     * @param apiinfo
     * @param add
     */
    void validApiInfo(Apiinfo apiinfo, boolean add);
}


