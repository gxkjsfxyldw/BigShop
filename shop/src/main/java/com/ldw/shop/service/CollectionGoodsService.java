package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.CollectionGoods;

public interface CollectionGoodsService extends IService<CollectionGoods> {

    /**
     * 用户收藏或取消收藏商品
     * @param userId
     * @param prodId
     */
    void addOrCancelProd(Integer userId, Long prodId);
}
