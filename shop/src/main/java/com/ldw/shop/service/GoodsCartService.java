package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.GoodsCart;
import com.ldw.shop.vo.param.ShopCatVo;
import org.quartz.SchedulerException;

import java.util.List;

public interface GoodsCartService extends IService<GoodsCart> {

    /**
     * * 添加商品到购物车
     * @param goodsCart
     */
    void changeItem(GoodsCart goodsCart) throws SchedulerException;

    /**
     * * 查看用户购物车列表
     * @param id
     */
    List<ShopCatVo> selectUserBasketInfo(Integer id);



}
