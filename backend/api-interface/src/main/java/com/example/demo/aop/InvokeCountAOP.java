package com.example.demo.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 调用次数 切面
 */
@Aspect
@Component
public class InvokeCountAOP {
    
    // 伪代码
    // 定义切面触发的时机（什么时候执行方法）：controller接口的方法执行成功后，执行下述方法

}
