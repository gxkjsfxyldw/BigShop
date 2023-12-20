package com.ldw.shop.config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *全局异常处理
 */
@RestControllerAdvice //表示是控制器的增强类
public class DataExceptionConfig {
    //捕捉异常，sql
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return ResponseEntity.badRequest().body("该数据有关联数据，操作失败！");
        }
        return ResponseEntity.badRequest().body("数据库异常，操作失败！");
    }
}
