package com.ldw.shop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.Sku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkuMapper extends BaseMapper<Sku> {
}
