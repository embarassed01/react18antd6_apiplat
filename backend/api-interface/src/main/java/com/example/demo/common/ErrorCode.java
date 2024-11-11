package com.example.demo.common;

/**
 * 错误码
 * 注意：`@Data`不支持枚举值，不能自动生成getter和setter，需要手动编写
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    FORBIDDEN(40300, "禁止访问(操作)", ""),
    NOT_FOUND_ERROR(40400, "请求数据不存在", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    OPERATION_ERROR(50001, "操作失败","");
    
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述（详情）
     * 具体原因，主要是给前端使用的！
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}