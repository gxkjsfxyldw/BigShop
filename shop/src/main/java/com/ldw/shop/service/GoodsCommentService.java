package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.GoodsComment;
import com.ldw.shop.vo.GoodsCommentVo;

public interface GoodsCommentService extends IService<GoodsComment> {

    /**
     * 查询单个商品评论总览信息
     * @param goodsId
     * @return
     */
    GoodsCommentVo selectGoodsCommOverview(Long goodsId);

    /**
     * *根据商品标识和评论级别查询商品评论，evaluate为null时查询商品的全部评论
     * @param page
     * @param goodsId
     * @param evaluate
     * @return
     */
    Page<GoodsComment> selectGoodsCommPageByProd(Page<GoodsComment> page, Long goodsId, Long evaluate);

    /**
     * * 添加商品评论
     * @param goodsComment
     */
    boolean addProdComm(GoodsComment goodsComment);


}
