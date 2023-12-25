package com.ldw.shop.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldw.shop.dao.pojo.TagGoods;
import com.ldw.shop.dao.pojo.TagGoodsReference;
import com.ldw.shop.service.TagGoodsReferenceService;
import com.ldw.shop.service.TagGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分组标签接口管理")
@RequestMapping("tagGoods")
@RestController
public class TagGoodsController  {

    @Autowired
    private TagGoodsService tagGoodsService;


    @ApiOperation("查询商品分组标签集合")
    @GetMapping("tagGoodsList")
    public ResponseEntity<List<TagGoods>> loadFrontTagGoodsList() {
        List<TagGoods> list = tagGoodsService.selectFrontTagGoodsList();
        return ResponseEntity.ok(list);
    }

}
