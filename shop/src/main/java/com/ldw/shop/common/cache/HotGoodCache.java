package com.ldw.shop.common.cache;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ldw.shop.common.constant.AbstractCache;
import com.ldw.shop.common.constant.CacheConstant;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class HotGoodCache extends AbstractCache {

    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void init() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(CacheConstant.NEWS_KEY))) {
            QueryWrapper<Goods> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("sold_num");
            //查询最高20条数据
            wrapper.last("limit 0,20");
            List<Goods> goods = goodsMapper.selectList(wrapper);
            redisTemplate.opsForValue().set(CacheConstant.NEWS_KEY, goods, 10, TimeUnit.MINUTES);
        }


    }

    @Override
    public <T> T get() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(CacheConstant.NEWS_KEY))) {
            reload();
        }
        return (T) redisTemplate.opsForValue().get(CacheConstant.NEWS_KEY);
    }

    @Override
    public void clear() {
        redisTemplate.delete(CacheConstant.NEWS_KEY);
    }
}
