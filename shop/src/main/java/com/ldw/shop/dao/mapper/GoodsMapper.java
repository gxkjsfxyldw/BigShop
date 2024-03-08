package com.ldw.shop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Update("update prod set total_stocks = total_stocks + #{count} , sold_num = sold_num - #{count} , version = version + 1 where prod_id = #{prodId} and version = #{version} and (total_stocks + #{count})>=0")
    int changeProdStock(@Param("prodId") Long prodId, @Param("count") Integer count, @Param("version") Integer version);

}
