package com.ldw.shop.vo.param;

import com.ldw.shop.dao.pojo.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("店铺订单对象")
public class ShopOrder {

    @ApiModelProperty("订单商品条目对象集合")
    private List<OrderItem> shopCartItemDiscounts;
}
