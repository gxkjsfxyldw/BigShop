package com.ldw.shop.controller;


import com.ldw.shop.dao.pojo.GoodsCart;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.GoodsCartService;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.CartTotalAmount;
import com.ldw.shop.vo.param.ShopCatVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "购物车模块")
@RequestMapping
@ApiModel("GoodsCatController")
public class GoodsCatController {

    @Autowired
    private GoodsCartService goodsCartService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @ApiOperation("添加商品到购物车或修改购物车中商品数量")
    @PostMapping("changeItem")
    public ResponseEntity<Void> changeItem(@RequestBody GoodsCart goodsCart) throws SchedulerException {
        User user = UserThreadLocal.get();
        goodsCart.setUserId(user.getId());
        goodsCartService.changeItem(goodsCart);
        return ResponseEntity.ok().build();
    }

    //    p/shopCart/info
    @ApiOperation("查询用户购物车列表")
    @GetMapping("info")
    public ResponseEntity<List<ShopCatVo>> loadUserCartVoInfo() {
        User user = UserThreadLocal.get();
        List<ShopCatVo> shopCatVos = goodsCartService.selectUserBasketInfo(user.getId());
        return ResponseEntity.ok(shopCatVos);
    }


    @ApiOperation("查询用户购物车中商品数量")
    @GetMapping("prodCount")
    public ResponseEntity<Long> loadUserBasketCount() {
        User user = UserThreadLocal.get();
        String redisKey = "cat" + "::" +user.getId();
        Long size = redisTemplate.opsForHash().size(redisKey);
        return ResponseEntity.ok(size);
    }


    //    p/shopCart/totalPay
    @ApiOperation("计算购物车中商品总金额")
    @PostMapping("totalPay")
    public ResponseEntity<CartTotalAmount> loadUserCartTotalAmount(@RequestBody List<Long> goodsCartList) {
        CartTotalAmount cartTotalAmount = goodsCartService.calculateUserCartTotalAmount(goodsCartList);
        return ResponseEntity.ok(cartTotalAmount);
    }


}
