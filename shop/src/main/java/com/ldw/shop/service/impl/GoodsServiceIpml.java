package com.ldw.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.cache.HotGoodCache;
import com.ldw.shop.common.constant.CacheConstant;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.mapper.CollectionGoodsMapper;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.mapper.SkuMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.GoodsCart;
import com.ldw.shop.dao.pojo.Sku;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.service.GoodsService;
import com.ldw.shop.vo.param.ChangeStock;
import com.ldw.shop.vo.param.GoodsChange;
import com.ldw.shop.vo.param.SkuChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GoodsServiceIpml extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;

    @Override
    public Page<Goods> selectGoodsPage(Page<Goods> page, Goods goods) {
        return goodsMapper.selectPage(page,new LambdaQueryWrapper<Goods>()
                .eq(ObjectUtil.isNotEmpty(goods.getStatus()),Goods::getStatus,goods.getStatus())
                .like(StringUtils.hasText(goods.getGoodsName()),Goods::getGoodsName,goods.getGoodsName())
                .orderByDesc(Goods::getPutawayTime,Goods::getCreateTime)
        );
    }

    /**
     * 先查询redis,看是否有缓存：
     *  有：则从redis里面获取后，直接返回
     *  没有：查询数据库，然后存入缓存
     * @return
     */
    @Override
    public Result getHotGoods() {
        Object redishotgoods = redisTemplate.opsForValue().get(CacheConstant.NEWS_KEY);
        if(redishotgoods!=null){
            return Result.success(redishotgoods);
        }
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sold_num");
        //查询最高20条数据
        wrapper.last("limit 0,20");
        List<Goods> goods = goodsMapper.selectList(wrapper);
        redisTemplate.opsForValue().set(CacheConstant.NEWS_KEY, goods, 10, TimeUnit.MINUTES);
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

    @Override
    public void changeStock(ChangeStock changeStock) {
        //获取商品sku库存数量对象
        List<SkuChange> skuChangeList = changeStock.getSkuChangeList();
        for (SkuChange skuChange:skuChangeList){
            Long skuId = skuChange.getSkuId();
            Sku sku = skuMapper.selectById(skuId);

            int i = skuMapper.changeSkuStock(skuId,skuChange.getCount(),sku.getVersion());
            if (i <= 0) {
                throw new RuntimeException("库存数量不足");
            }
        }
        //获取商品prod库存数量对象
        List<GoodsChange> prodChangeList = changeStock.getProdChangeList();
        for (GoodsChange prodChange:prodChangeList){
            Long prodId = prodChange.getProdId();
            Goods prod = goodsMapper.selectById(prodId);

            int i = goodsMapper.changeProdStock(prodId,prodChange.getCount(),prod.getVersion());
            if (i <= 0) {
                throw new RuntimeException("库存数量不足");
            }
        }
    }
}
