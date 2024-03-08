package com.ldw.shop.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("订单商品prodId和购买数量")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsChange {

    @ApiModelProperty("商品prodId")
    private Long prodId;

    @ApiModelProperty("商品prod购买数量")
    private Integer count;
}
