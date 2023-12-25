package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.TagGoods;

import java.util.List;

public interface TagGoodsService extends IService<TagGoods> {
    /**
     * 查询商品分组标签集合
     * @return
     */
    List<TagGoods> selectFrontTagGoodsList();
}
