package com.ldw.shop.common.aop;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface ApiCall {

    /**
     * 限流次数, 单位时间内能访问多少次，默认无限
     */
    int limit() default 1000000 ;
    /**
     * 限流时间,单位秒 默认60秒
     */
    long time() default 60; // 时间段
    /**
     * 时间类型,默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
