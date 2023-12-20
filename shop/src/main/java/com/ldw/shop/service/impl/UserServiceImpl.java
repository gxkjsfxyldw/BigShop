package com.ldw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.UserMapper;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public User checkUser(String username, String password) {

        return  userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername,username)
                .eq(User::getPassword,password)
        );
    }
}
