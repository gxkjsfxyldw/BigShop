package com.ldw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.TagGoodsMapper;
import com.ldw.shop.dao.mapper.UserMapper;
import com.ldw.shop.dao.pojo.TagGoods;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.TagGoodsService;
import com.ldw.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagGoodsServiceImpl extends ServiceImpl<TagGoodsMapper, TagGoods> implements TagGoodsService {
    @Autowired TagGoodsMapper tagGoodsMapper;

    @Override
    public List<TagGoods> selectFrontTagGoodsList() {
        return tagGoodsMapper.selectList(new LambdaQueryWrapper<TagGoods>()
                .eq(TagGoods::getStatus,1)
                .orderByDesc(TagGoods::getSeq)
        );
    }
}
