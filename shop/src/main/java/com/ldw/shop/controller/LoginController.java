package com.ldw.shop.controller;


import com.ldw.shop.common.aop.LogAnnotation;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.LoginService;
import com.ldw.shop.vo.param.LoginVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiModel("LoginController")
public class LoginController {

    @Autowired
    private LoginService loginService;

//    @LogAnnotation(module="登录模块",opertion = "用户登录")
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        return loginService.login(loginVo);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        return ResponseEntity.ok(loginService.register(user));
    }

}
