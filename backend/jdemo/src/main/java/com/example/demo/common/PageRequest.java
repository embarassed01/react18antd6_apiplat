package com.example.demo.common;

import java.io.Serializable;

import lombok.Data;

import static com.example.demo.constant.CommonConstant.*;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {
    // 序列化UID，可以使对象在序列化的时候保持唯一！
    private static final long serialVersionUID = 22L;
    
    /**
     * 页面大小
     */
    protected long pageSize = 10;

    /**
     * 当前是第几页(当前页号)
     */
    protected long current = 1;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SORT_ORDER_ASC;
}
