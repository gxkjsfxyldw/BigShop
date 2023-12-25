package com.ldw.shop.dao.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 单品SKU表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sku")
public class Sku implements Serializable {
    /**
     * 单品ID
     */
    @TableId(value = "sku_id", type = IdType.AUTO)
    private Long skuId;

    /**
     * 商品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 销售属性组合字符串 格式是p1:v1;p2:v2
     */
    @TableField(value = "properties")
    private String properties;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    private BigDecimal oriPrice;

    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品在付款减库存的状态下，该sku上未付款的订单数量
     */
    @TableField(value = "stocks")
    private Integer stocks;

    /**
     * 实际库存
     */
    @TableField(value = "actual_stocks")
    private Integer actualStocks;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 记录时间
     */
    @TableField(value = "rec_time")
    private Date recTime;

    /**
     * 商家编码
     */
    @TableField(value = "party_code")
    private String partyCode;

    /**
     * 商品条形码
     */
    @TableField(value = "model_id")
    private String modelId;

    /**
     * sku图片
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * sku名称
     */
    @TableField(value = "sku_name")
    private String skuName;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    private String prodName;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 商品重量
     */
    @TableField(value = "weight")
    private Double weight;

    /**
     * 商品体积
     */
    @TableField(value = "volume")
    private Double volume;

    /**
     * 0 禁用 1 启用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0 正常 1 已被删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
