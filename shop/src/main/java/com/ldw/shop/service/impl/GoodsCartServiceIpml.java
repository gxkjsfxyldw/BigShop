package com.ldw.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.common.constant.ScheduConstant;
import com.ldw.shop.dao.mapper.GoodsCartMapper;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.mapper.ShopMapper;
import com.ldw.shop.dao.mapper.SkuMapper;
import com.ldw.shop.dao.pojo.*;
import com.ldw.shop.handle.GoodsCartUpdateTask;
import com.ldw.shop.service.GoodsCartService;
import com.ldw.shop.utils.QuartzUtils;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.CartItem;
import com.ldw.shop.vo.param.ShopCatVo;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GoodsCartServiceIpml extends ServiceImpl<GoodsCartMapper, GoodsCart> implements GoodsCartService {

    @Autowired
    private GoodsCartMapper goodsCartMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //过期时间
    private long expire= 1;

    /**
     * 添加商品到购物车表 或者更新购物车商品数量
     * 1.根据商品skuid和用户标识查询购物车中商品是否存在
     *    查redis：存在商品id：则更新
     *            不存在商品id：则新增
     *
     * @param goodsCart
     */
    @Override
    public void changeItem(GoodsCart goodsCart) throws SchedulerException {
        //先从redis里面获取key
        String redisKey = "cat" + "::" +goodsCart.getUserId();
        Object rediscart = redisTemplate.opsForHash().get(redisKey, goodsCart.getProdId());
        GoodsCart oldcart =JSON.parseObject(String.valueOf(rediscart), GoodsCart.class);
        //判断是否存在  不存在：插入数据库
        if (ObjectUtil.isNull(oldcart)) {
            //删除缓存
            redisTemplate.delete(redisKey);
            //将商品添加到购物车中
            goodsCart.setCartDate(new Date());
            goodsCartMapper.insert(goodsCart);
            //再次删除缓存
            redisTemplate.delete(redisKey);
            return;
        }
        //存在：更新商品中购物车中的数量即可   更新缓存
        oldcart.setCartCount(goodsCart.getCartCount());
        oldcart.setSkuId(goodsCart.getSkuId());
        oldcart.setCartDate(new Date());

        Map newMap = new HashMap();
        newMap.put(oldcart.getProdId(),JSON.toJSONString(oldcart));
        redisTemplate.opsForHash().putAll(redisKey,newMap);

        // 定时更新到数据库
        GoodsCartUpdateTask goodsCartUpdateTask = new GoodsCartUpdateTask();
        QuartzUtils quartzUtils = new QuartzUtils(ScheduConstant.GoodsCart_TASK_IDENTITY);
        try {
            quartzUtils.jobDetail(goodsCartUpdateTask);
        }finally {
            quartzUtils.startScheduTask(goodsCartUpdateTask,oldcart.getProdId(),goodsCart.getUserId());
        }
    }
    public Boolean updateGoodsCart(Long prodId,Integer userid){
        //先从redis里面获取key
        String redisKey = "cat" + "::" + userid;
        Object rediscart = redisTemplate.opsForHash().get(redisKey,prodId);
        GoodsCart updategoods =JSON.parseObject(String.valueOf(rediscart), GoodsCart.class);
        if(updategoods!=null){
            goodsCartMapper.updateById(updategoods);
            return true;
        }
        return false;
    }

    /**
     * * 查看用户购物车列表
     * @param userid
     */
    @Override
    public List<ShopCatVo> selectUserBasketInfo(Integer userid) {
        String redisKey = "cat" + "::" +userid;
        List<GoodsCart> goodsCartlist = new ArrayList<>();
        Map<Object, Object> usercart = redisTemplate.opsForHash().entries(redisKey);
        System.out.println(usercart);
        //遍历这个map
        if(usercart.isEmpty()){
            //先判断缓存有没有 就判断有没有 userid这个键就行了
            System.out.println("数据库");
            goodsCartlist = goodsCartMapper.selectList(new LambdaQueryWrapper<GoodsCart>().eq(GoodsCart::getUserId, userid));

        }else{
            System.out.println("缓存");
            for (Object shopid:usercart.keySet()) {
//                goodsCartlist.add((GoodsCart) usercart.get(shopid));
                goodsCartlist.add(JSON.parseObject(String.valueOf(usercart.get(shopid)), GoodsCart.class));
            }
        }
        Map<Long, String> shopmap = new HashMap<>();
        List<CartItem> newgoods=new LinkedList<>();
        //遍历用户购物车,将商店集合与商品集合信息整理出来
        goodsCartlist.forEach( goodscat ->{
            //找到该商品属于的商店
            if(!shopmap.containsKey(goodscat.getShopId())){//存在Key返回true
                //查询条件
                LambdaQueryWrapper<Shop> shopqueryWrapper = new LambdaQueryWrapper<Shop>().eq(Shop::getShopId, goodscat.getShopId());
                //需要查找的字段
                shopqueryWrapper.select(Shop::getShopId, Shop::getShopName);
                Shop nowshop = shopMapper.selectOne(shopqueryWrapper);
                shopmap.put(nowshop.getShopId(),nowshop.getShopName());
            }

            //找到该商品信息
            LambdaQueryWrapper<Goods> goodsqueryWrapper = new LambdaQueryWrapper<Goods>().eq(Goods::getGoodsId, goodscat.getProdId());
            //需要查找的字段
            goodsqueryWrapper.select(Goods::getGoodsId, Goods::getGoodsName,Goods::getPic,Goods::getPrice,Goods::getShopId);
            Goods nowgoods = goodsMapper.selectOne(goodsqueryWrapper);
            CartItem cartItem = new CartItem();
            cartItem.setCartCount(goodscat.getCartCount());
            cartItem.setGoodsId(nowgoods.getGoodsId());
            cartItem.setGoodsName(nowgoods.getGoodsName());
            cartItem.setPic(nowgoods.getPic());
            cartItem.setPrice(nowgoods.getPrice());
            cartItem.setShopId(nowgoods.getShopId());

            //找到该商品的sku信息
            LambdaQueryWrapper<Sku> skuqueryWrapper = new LambdaQueryWrapper<Sku>().eq(Sku::getProdId, goodscat.getProdId()).eq(Sku::getSkuId,goodscat.getSkuId());
            //需要查找的字段
            skuqueryWrapper.select(Sku::getSkuId, Sku::getSkuName);
            Sku nowsku = skuMapper.selectOne(skuqueryWrapper);
            cartItem.setSkuId(nowsku.getSkuId());
            cartItem.setSkuName(nowsku.getSkuName());
            newgoods.add(cartItem);

            //存储到redis 以用户id为键，每个商品id为hash键，购物车记录为hash值，到时候删改时就直接操作该数据即可
            redisTemplate.opsForHash().put(redisKey,goodscat.getProdId(),JSON.toJSONString(goodscat));
            redisTemplate.opsForHash().getOperations().expire(redisKey,expire, TimeUnit.DAYS);//过期时间1天
        });

        List<ShopCatVo> newcat=new ArrayList<>();//用户购物车
        //遍历商店
        for (Long shopid:shopmap.keySet()) {
            ShopCatVo shopCatVo = new ShopCatVo();
            List<CartItem> collect = newgoods.stream().filter(goods -> goods.getShopId().equals(shopid)).collect(Collectors.toList());
            CartItem cartItem = collect.get(0);
            shopCatVo.setShopId(cartItem.getShopId());
            shopCatVo.setShopName(shopmap.get(cartItem.getShopId()));
            shopCatVo.setShopCartItems(collect);
            newcat.add(shopCatVo);
        }
        return newcat;
    }
}
