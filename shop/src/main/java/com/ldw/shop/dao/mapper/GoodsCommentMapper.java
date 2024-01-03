package com.ldw.shop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldw.shop.dao.pojo.GoodsComment;
import com.ldw.shop.vo.GoodsCommentVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsCommentMapper extends BaseMapper<GoodsComment> {
    /**
     *
     * @param goodsId
     * @return
     */
    void selectComm(GoodsComment goodsId);
}
