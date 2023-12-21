package com.ldw.shop.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ldw.shop.dao.pojo.CollectionGoods;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.utils.UserThreadLocal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "用户收藏商品接口管理")
@RestController
@RequestMapping("collection")
@ApiModel("CollectionGoodsController")
public class CollectionGoodsController {


    @Autowired
    private CollectionGoodsService collectionGoodsService;

    @ApiOperation("查询用户收藏商品的数量")
    @GetMapping("/count")
    public ResponseEntity<Integer> loadUserCollectionCount() {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        int count = collectionGoodsService.count(new LambdaQueryWrapper<CollectionGoods>()
                .eq(CollectionGoods::getUserId, userId)
        );
        return ResponseEntity.ok(count);
    }

    //    collection/isCollection?goodsId=95
    @ApiOperation("用户是否收藏当前商品")
    @GetMapping("isCollection")
    public ResponseEntity<Boolean> isCollection(@RequestParam Long goodsId) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        int count = collectionGoodsService.count(new LambdaQueryWrapper<CollectionGoods>()
                .eq(CollectionGoods::getUserId, userId)
                .eq(CollectionGoods::getProdId, goodsId)
        );
        return ResponseEntity.ok(count == 1);
    }

    //    collection/addOrCancel
    @ApiOperation("用户收藏或取消收藏商品")
    @PostMapping("addOrCancel")
    public ResponseEntity<Void> addOrCancelProd(@RequestBody Long prodId) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        collectionGoodsService.addOrCancelProd(userId,prodId);
        return ResponseEntity.ok().build();
    }


}
