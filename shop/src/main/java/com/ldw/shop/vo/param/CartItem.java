package com.ldw.shop.vo.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("购物车商品")
public class CartItem {

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品skuId")
    private Long skuId;

    @ApiModelProperty("商品sku名称")
    private String skuName;

    @ApiModelProperty("商品图片")
    private String pic;

    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("该产品购物车个数")
    private Integer cartCount;

    @ApiModelProperty("店铺id")
    private Long shopId;

}
