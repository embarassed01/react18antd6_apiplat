package com.example.demo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Error;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.ApiinfoMapper;
import com.example.demo.model.entity.Apiinfo;
import com.example.demo.service.ApiinfoService;

/**
 * @description 针对apiinfo表的数据库操作Service的实现
 */
@Service
public class ApiinfoServiceImpl extends ServiceImpl<ApiinfoMapper,Apiinfo> implements ApiinfoService {

    @Override
    public void validApiInfo(Apiinfo apiinfo, boolean add) {
        if (apiinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = apiinfo.getName();
        String description = apiinfo.getDescription();  // can null
        String method = apiinfo.getMethod();
        String url = apiinfo.getUrl();
        String requestHeader = apiinfo.getRequestHeader();
        String responseHeader = apiinfo.getResponseHeader();
        Integer status = apiinfo.getStatus();  // can null (default 0)
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, method, url, requestHeader, responseHeader)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
    }
    
}
