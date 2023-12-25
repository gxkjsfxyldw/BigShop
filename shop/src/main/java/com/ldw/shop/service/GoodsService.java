package com.ldw.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.Goods;

import java.util.List;

public interface GoodsService extends IService<Goods> {

    /**
     * * 多条件分页查询商品列表
     * @param page
     * @param goods
     * @return
     */
    Page<Goods> selectGoodsPage(Page<Goods> page, Goods goods);

    /**
     * * 热门商品
     * @return
     */
    Result getHotGoods();

    /**
     * *
     * @param goodsId
     * @return
     */
    Goods selectProdAndSkuDetailById(Long goodsId);
}
