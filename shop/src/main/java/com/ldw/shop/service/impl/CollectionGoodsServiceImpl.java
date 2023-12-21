package com.ldw.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.CollectionGoodsMapper;
import com.ldw.shop.dao.mapper.UserMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.service.LoginService;
import com.ldw.shop.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CollectionGoodsServiceImpl extends ServiceImpl<CollectionGoodsMapper, CollectionGoods> implements CollectionGoodsService {

    @Autowired
    private CollectionGoodsMapper collectionGoodsMapper;

    @Override
    public void addOrCancelProd(Integer userId, Long prodId) {
        //根据用户标识和商品标识查询当前状态
        CollectionGoods userCollection = collectionGoodsMapper.selectOne(new LambdaQueryWrapper<CollectionGoods>()
                .eq(CollectionGoods::getUserId, userId)
                .eq(CollectionGoods::getProdId, prodId)
        );
        //判断是否存在
        if (ObjectUtil.isNull(userCollection)) {
            //添加收藏
            userCollection = new CollectionGoods();
            userCollection.setUserId(String.valueOf(userId));
            userCollection.setProdId(prodId);
            userCollection.setCreateTime(new Date());
            collectionGoodsMapper.insert(userCollection);
        } else {
            //取消收藏
            collectionGoodsMapper.deleteById(userCollection.getId());
        }

    }
}
