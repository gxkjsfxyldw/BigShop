package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.vo.param.UserVo;

public interface UserService extends IService<User> {
    /**
     * 用户登录校验
     */
    User checkUser(String username,String password);
    /**
     * 根据用户名查询
     */
    Boolean selecetUserByUsername(String username);

    /**
     * * 用户个人信息修改
     * @param userVo
     * @return
     */
    Result updateUser(UserVo userVo);
}
