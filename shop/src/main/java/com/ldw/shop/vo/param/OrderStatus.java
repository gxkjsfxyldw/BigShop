package com.ldw.shop.vo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("订单总览信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatus {

    @ApiModelProperty("待支付数量")
    private Integer unPay;

    @ApiModelProperty("待发货数量")
    private Integer payed;

    @ApiModelProperty("待收货数量")
    private Integer consignment;
}
