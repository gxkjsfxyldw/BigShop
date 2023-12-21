package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.UserAddress;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {

    /**
     * * 查询用户收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> selectOrderAddrList(Integer userId);

    /**
     * *删除用户收货地址
     * @param userId
     * @param addrId
     */
    void deleteUserAddr(Integer userId, Long addrId);

    /**
     * * 设置用户默认收货地址
     * @param userId
     * @param addrId
     */
    void updateUserDefaultAddr(Integer userId, Long addrId);
}
