package com.example.demo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.apicommon.model.entity.User;
import com.example.apicommon.service.InnerUserService;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserMapper;

import jakarta.annotation.Resource;

@Component
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accesssKey) {
        if (StringUtils.isAnyBlank(accesssKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accesssKey);  //ak应该也是唯一的
        // queryWrapper.eq("secretKey", secretKey);
        return userMapper.selectOne(queryWrapper);
    }
    
}
