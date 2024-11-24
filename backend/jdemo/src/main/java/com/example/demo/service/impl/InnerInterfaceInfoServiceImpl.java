package com.example.demo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.apicommon.model.entity.Apiinfo;
import com.example.apicommon.service.InnerInterfaceInfoService;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.ApiinfoMapper;

import jakarta.annotation.Resource;

@Component
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private ApiinfoMapper apiinfoMapper;

    @Override
    public Apiinfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Apiinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return apiinfoMapper.selectOne(queryWrapper);
    }
    
}
