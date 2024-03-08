package com.ldw.shop.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("订单商品skuId和购买数量")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuChange {

    @ApiModelProperty("商品skuId")
    private Long skuId;

    @ApiModelProperty("商品sku购买数量")
    private Integer count;
}
