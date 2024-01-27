package com.ldw.shop.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("购物车总金额对象")
public class CartTotalAmount {

    @ApiModelProperty("总额")
    private BigDecimal totalMoney = BigDecimal.ZERO;

    @ApiModelProperty("优惠金额")
    private BigDecimal subtractMoney = BigDecimal.ZERO;

    @ApiModelProperty("合计")
    private BigDecimal finalMoney = BigDecimal.ZERO;
}
