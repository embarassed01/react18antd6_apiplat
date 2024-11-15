package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserInterfaceInfoMapper;
import com.example.demo.model.entity.UserInterfaceInfo;
import com.example.demo.service.UserInterfaceInfoService;

/**
 * @description 
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper,UserInterfaceInfo> implements UserInterfaceInfoService {

    @Override
    public void validUesrInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.gt("leftNum", 0);  // great must >0
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");  // TODO 需要加锁！！
        return this.update(updateWrapper);
        /*
         经mock数据sql测试，🆗
        INSERT INTO api.user_interface_info (userId, interfaceInfoId, totalNum, leftNum) VALUES(1, 1, 2, 5);
        update api.user_interface_info  set leftNum = leftNum -1, totalNum = totalNum + 1 where interfaceInfoId  = 1 and userId =1;
         */
    }
    
}
