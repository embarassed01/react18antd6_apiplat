package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 权限校验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    
    /**
     * 有任何一个角色
     * @return
     */
    String[] anyRole() default "";

    /**
     * 必须有某个角色
     * @return
     */
    String mustRole() default "";
}
