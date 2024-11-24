package com.example.demo.model.vo;

import com.example.apicommon.model.entity.Apiinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息封装视图
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends Apiinfo {
    /**
     * 调用次数
     */
    private Integer totalNum;
}
