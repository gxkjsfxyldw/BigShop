package com.ldw.shop.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseBody;
//主动抛出异常
//对加了controller注解的方法进行拦截处理 AOP的实现
@ControllerAdvice
public class AllExceptionConfig {
    //进行异常处理， 处理Exception.class的异常
    @ExceptionHandler (Exception.class)
    @ResponseBody //加它返回json数据，不加返回界面
    public ResponseEntity<String> doException(Exception ex){
        ex.printStackTrace();
        return ResponseEntity.badRequest().body("系统开小差了，请重试");
    }
}
