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
import java.util.Date;

/**
 * 购物车
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "goods_cart")
public class GoodsCart implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * SkuID
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 购物车产品个数
     */
    @TableField(value = "cart_count")
    private Integer cartCount;

    /**
     * 购物时间
     */
    @TableField(value = "cart_date")
    private Date cartDate;

    /**
     * 满减活动ID
     */
    @TableField(value = "discount_id")
    private Long discountId;


    private static final long serialVersionUID = 1L;
}
