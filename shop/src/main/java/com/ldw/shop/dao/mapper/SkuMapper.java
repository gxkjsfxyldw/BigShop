package com.ldw.shop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SkuMapper extends BaseMapper<Sku> {
    @Update("update sku set actual_stocks = actual_stocks + #{count},stocks = stocks - #{count},version = version + 1 where sku_id = #{skuId} and version = #{version} and (actual_stocks + #{count})>=0 ")
    int changeSkuStock(@Param("skuId") Long skuId, @Param("count") Integer count, @Param("version") Integer version);

}
