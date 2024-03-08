package com.ldw.shop.vo.param;


import com.ldw.shop.dao.pojo.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单确认对象")
public class OrderVo {

    @ApiModelProperty("收货地址对象")
    private UserAddress userAddress;

    @ApiModelProperty("店铺对象集合")
    private List<ShopOrder> shopCartOrders;

    @ApiModelProperty("订单商品总数量")
    private Integer totalCount;


    @ApiModelProperty("商品总价")
    private BigDecimal total = BigDecimal.ZERO;

    @ApiModelProperty("运费")
    private BigDecimal transfee = BigDecimal.ZERO;

    @ApiModelProperty("优惠金额")
    private BigDecimal shopReduce = BigDecimal.ZERO;

    @ApiModelProperty("需付款")
    private BigDecimal actualTotal = BigDecimal.ZERO;

    @ApiModelProperty("确认订单来自于哪儿，0商品详情页面，1购物车")
    private Integer source = 0;

    @ApiModelProperty("订单备注")
    private String remarks;
}
