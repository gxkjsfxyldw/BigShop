package com.ldw.shop.handle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ldw.shop.common.constant.CacheConstant;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.pojo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class HotGoodsCacheSchedul {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GoodsMapper goodsMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        boolean result = Boolean.FALSE.equals(redisTemplate.hasKey(CacheConstant.NEWS_KEY));
        System.out.println("*************定时任务*****************"+ result);
        if (result) {
            QueryWrapper<Goods> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("sold_num");
            //查询最高20条数据
            wrapper.last("limit 0,20");
            List<Goods> goods = goodsMapper.selectList(wrapper);
            redisTemplate.opsForValue().set(CacheConstant.NEWS_KEY, goods, 10, TimeUnit.MINUTES);
        }
    }

}
