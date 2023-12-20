package com.ldw.shop.service;


import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.vo.param.LoginVo;

public interface LoginService {
    /**
     * 用户登录，返回token
     */
    Result login(LoginVo loginVo);

    /**
     * * 校验token
     * @param token
     * @return
     */
    User chckenToken(String token);

    /**
     * 注册
     * @param user
     * @return
     */
    String register(User user);
}
