package com.ldw.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.UserAddressMapper;
import com.ldw.shop.dao.pojo.UserAddress;
import com.ldw.shop.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAddressServiceIpml extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> selectOrderAddrList(Integer userId) {
        return userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId,userId)
                .eq(UserAddress::getStatus,1)
                .orderByDesc(UserAddress::getCommonAddr, UserAddress::getCreateTime)
        );
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteUserAddr(Integer userId, Long addrId) {
        //查询当前用户的默认收货地址
        UserAddress userAddr = userAddressMapper.selectOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getCommonAddr, 1)
        );
        //获取默认收货地址标识
        Long defaultAddrId = userAddr.getAddrId();
        //判断当前删除的收货地址是否为默认收货地址
        if (defaultAddrId.equals(addrId)) {
            //是：再去重新找一个新的收货地址并设置为默认收货地址
            //把最近添加的收货地址设置为默认收货地址
            List<UserAddress> userAddrList = userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                    .eq(UserAddress::getUserId, userId)
                    .eq(UserAddress::getStatus, 1)
                    .orderByDesc(UserAddress::getCreateTime)
            );
            //判断是否有值  是否有地址的存在
            if (CollectionUtil.isNotEmpty(userAddrList) && userAddrList.size() != 0) {
                UserAddress defaultUserAddr = userAddrList.get(0);
                defaultUserAddr.setCommonAddr(1);
                userAddressMapper.updateById(defaultUserAddr);
            }
        }
        //如果不是默认收货地址：删除
        userAddressMapper.deleteById(addrId);
    }
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void updateUserDefaultAddr(Integer userId, Long addrId) {
        //查询用户默认收货地址
        UserAddress oldDefaultUserAddr = userAddressMapper.selectOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getStatus, 1)
                .eq(UserAddress::getCommonAddr, 1)
        );
        System.out.println(oldDefaultUserAddr);
        //判断默认收货地址与新的默认收货地址是否一致
        if (oldDefaultUserAddr.getAddrId().equals(addrId)|| ObjectUtil.isEmpty(oldDefaultUserAddr)) {
            //一致：结束
            return;
        }
        //不一致：更新
        oldDefaultUserAddr.setCommonAddr(0);
        userAddressMapper.updateById(oldDefaultUserAddr);
        //更新新的默认收货地址
        UserAddress newUserAddr = new UserAddress();
        newUserAddr.setAddrId(addrId);
        newUserAddr.setCommonAddr(1);
        userAddressMapper.updateById(newUserAddr);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean save(UserAddress userAddr) {
        //查询用户收货地址列表
        List<UserAddress> userAddrList = userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userAddr.getUserId())
                .eq(UserAddress::getStatus, 1)
        );
        userAddr.setCommonAddr(0);
        userAddr.setStatus(1);
        userAddr.setCreateTime(new Date());
        userAddr.setUpdateTime(new Date());
        userAddr.setVersion(0);
        //判断当前用户是否有收货地址
        if (CollectionUtil.isEmpty(userAddrList) || userAddrList.size() == 0) {
            //说明：用户没有收货地址，那当前新增的就为默认收货地址
            userAddr.setCommonAddr(1);
        }
        return userAddressMapper.insert(userAddr)>0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateById(UserAddress userAddr) {
        userAddr.setUpdateTime(new Date());
        return userAddressMapper.updateById(userAddr)>0;
    }


}
