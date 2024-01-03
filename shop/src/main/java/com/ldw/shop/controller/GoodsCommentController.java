package com.ldw.shop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldw.shop.dao.pojo.GoodsComment;
import com.ldw.shop.service.GoodsCommentService;
import com.ldw.shop.vo.GoodsCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "商品评论模块")
@RequestMapping("/comment")
public class GoodsCommentController {

    @Autowired
    private GoodsCommentService commentService;

    @ApiOperation("查询单个商品评论总览信息")
    @GetMapping("GoodsCommTotalInfo")
    public ResponseEntity<GoodsCommentVo> loadGoodsCommOverview(@RequestParam Long goodsId) {
        GoodsCommentVo goodsCommentVo = commentService.selectGoodsCommOverview(goodsId);
        return ResponseEntity.ok(goodsCommentVo);
    }

    @ApiOperation("根据商品标识和评论级别查询商品评论，evaluate为null时查询商品的全部评论")
    @GetMapping("CoodsCommByGoodsId")
    public ResponseEntity<Page<GoodsComment>> loadGoodsCommPageByProd(Page<GoodsComment> page, Long goodsId, Long evaluate) {
        page = commentService.selectGoodsCommPageByProd(page,goodsId,evaluate);
        return ResponseEntity.ok(page);
    }

    @ApiOperation("根据评论id查询评论详情")
    @GetMapping("{commId}")
    public ResponseEntity<GoodsComment> loadProdCommInfo(@PathVariable Long commId) {
        GoodsComment goodsComment = commentService.getById(commId);
        return ResponseEntity.ok(goodsComment);
    }

    @ApiOperation("商品评论")
    @PostMapping
    public ResponseEntity<Boolean> addProdComm(@RequestBody GoodsComment goodsComment) {
        boolean result = commentService.addProdComm(goodsComment);
        return ResponseEntity.ok(result);
    }
    @ApiOperation("商家回复并审核评论")
    @PutMapping
    public ResponseEntity<Boolean> updateProdComm(@RequestBody GoodsComment prodComm) {
        boolean result = commentService.updateById(prodComm);
        return ResponseEntity.ok(result);
    }
}
