package com.ldw.shop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
