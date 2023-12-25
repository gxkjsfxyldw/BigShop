package com.ldw.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.mapper.SkuMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.Sku;
import com.ldw.shop.service.GoodsService;
import com.ldw.shop.service.SkuService;
import org.springframework.stereotype.Service;

@Service
public class SkuServiceIpml extends ServiceImpl<SkuMapper, Sku> implements SkuService {
}
