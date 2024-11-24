package com.example.demo.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    
    // select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}