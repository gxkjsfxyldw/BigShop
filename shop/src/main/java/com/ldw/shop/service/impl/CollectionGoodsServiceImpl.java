package com.ldw.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.constant.ScheduConstant;
import com.ldw.shop.dao.mapper.CollectionGoodsMapper;
import com.ldw.shop.dao.mapper.UserMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.utils.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import com.ldw.shop.handle.CollectionSaveTask;


@Service
@Slf4j
public class CollectionGoodsServiceImpl extends ServiceImpl<CollectionGoodsMapper, CollectionGoods> implements CollectionGoodsService {

    @Autowired
    private CollectionGoodsMapper collectionGoodsMapper;

    @Override
    public void addOrCancelProd(Integer userId, Long prodId) throws SchedulerException {
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

//        QuartzUtils quartzUtils = new QuartzUtils(ScheduConstant.Collection_TASK_IDENTITY);
//        quartzUtils.startScheduTask();
    }




}
