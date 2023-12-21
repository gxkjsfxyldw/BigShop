package com.ldw.shop.controller;

import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.UserService;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@ApiModel("UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiModelProperty("用户获取个人信息")
    @GetMapping("/info")
    public ResponseEntity<User> getUserInfomation(){
        return ResponseEntity.ok(UserThreadLocal.get());
    }

    @ApiModelProperty("用户修改个人信息")
    @PostMapping("/modify")
    public Result modify(@RequestBody UserVo userVo){
        return userService.updateUser(userVo);
    }



}
