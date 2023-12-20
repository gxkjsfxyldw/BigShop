package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.User;

public interface UserService extends IService<User> {
    /**
     * 用户登录校验
     */
    User checkUser(String username,String password);

}
