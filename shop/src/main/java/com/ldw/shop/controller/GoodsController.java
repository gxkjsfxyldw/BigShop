package com.ldw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.TagGoodsReference;
import com.ldw.shop.service.GoodsService;
import com.ldw.shop.service.SkuService;
import com.ldw.shop.service.TagGoodsReferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(tags = "商品接口管理")
@RequestMapping("goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TagGoodsReferenceService tagGoodsReferenceService;
    private Page<TagGoodsReference> tagGoodsReferencePage;

    //分页展示商品信息  可根据商品名称等进行查询
    //http://localhost:8086/goods/page? size=2 & current=1 & goodsName = 手
    @ApiOperation("多条件分页查询商品列表")
    @GetMapping("/page")
    public ResponseEntity<Page<Goods>> loadProdPage(Page<Goods> page, Goods goods) {
        page = goodsService.selectGoodsPage(page,goods);
        return ResponseEntity.ok(page);
    }

    //根据id查询商品
    @ApiOperation("根据标识查询商品详情")
    @GetMapping("/info/{goodId}")
    public ResponseEntity<Goods> loadProdInfo(@PathVariable Long goodId) {
        Goods goods = goodsService.getById(goodId);
        return ResponseEntity.ok(goods);
    }

    //热门商品 销量前20个商品
    @ApiOperation("获取销量前20的热门商品")
    @GetMapping("/hot")
    public Result getHotProduct() {
        return Result.success(goodsService.getHotGoods());
    }

//  getGoodInfoSku?goodId=60
    @ApiOperation("根据标识查询商品详情和商品sku")
    @GetMapping("/getGoodInfoSku")
    public ResponseEntity<Goods> loadGoodsAndSkuDetail(@RequestParam Long goodsId) {
        Goods goods = goodsService.selectProdAndSkuDetailById(goodsId);
        return ResponseEntity.ok(goods);
    }

    @ApiOperation("根据商品分组标签查询商品列表")
    @GetMapping("getTagGoodsReferencePageByTagId")
    public Page<Goods> getTagGoodsReferencePageByTagId(@RequestParam Long tagId, @RequestParam Long size) {

        Page<TagGoodsReference> tagGoodsReferencePage = new Page<>(1,size);
        Page<TagGoodsReference> page = tagGoodsReferenceService.page(tagGoodsReferencePage, new LambdaQueryWrapper<TagGoodsReference>().eq(TagGoodsReference::getTagId, tagId));
        List<TagGoodsReference> goodsReferenceList = page.getRecords();
        //过滤商品id
        List<Long> taglist = goodsReferenceList.stream().map(TagGoodsReference::getShopId).collect(Collectors.toList());
        //根据商品id查询商品列表详情
        List<Goods> goods = goodsService.listByIds(taglist);
        Page<Goods> resultpage = new Page<>();
        return resultpage.setRecords(goods);
    }
}
