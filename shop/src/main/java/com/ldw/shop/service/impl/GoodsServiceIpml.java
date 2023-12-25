package com.ldw.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.mapper.CollectionGoodsMapper;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.mapper.SkuMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.Sku;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodsServiceIpml extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public Page<Goods> selectGoodsPage(Page<Goods> page, Goods goods) {
        return goodsMapper.selectPage(page,new LambdaQueryWrapper<Goods>()
                .eq(ObjectUtil.isNotEmpty(goods.getStatus()),Goods::getStatus,goods.getStatus())
                .like(StringUtils.hasText(goods.getGoodsName()),Goods::getGoodsName,goods.getGoodsName())
                .orderByDesc(Goods::getPutawayTime,Goods::getCreateTime)
        );
    }

    @Override
    public Result getHotGoods() {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sold_num");
        //查询最高20条数据
        wrapper.last("limit 0,20");
        List<Goods> goods = goodsMapper.selectList(wrapper);
        return Result.success(goods);
    }

    @Override
    public Goods selectProdAndSkuDetailById(Long goodsId) {
        //根据标识查询商品详情
        Goods goods = goodsMapper.selectById(goodsId);
        //根据标识查询商品sku详情
        List<Sku> skuList = skuMapper.selectList(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getProdId, goodsId)
                .eq(Sku::getStatus, 1)
        );
        goods.setSkuList(skuList);
        return goods;
    }
}
