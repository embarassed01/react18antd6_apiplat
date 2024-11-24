package com.example.apicommon.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 */
public enum ApiinfoStatusEnum {
    ONLINE("发布", 1),
    OFFLINE("下线", 0);

    private final String text;
    private final int value;

    ApiinfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    /**
     * 获取 值列表
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }
}

