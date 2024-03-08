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
@ApiModel("订单确认参数对象")
public class OrderConfirmDto {
    @ApiModelProperty("订单商品条目对象")
    private OrderItem orderItem;

    @ApiModelProperty("购物车id集合")
    private List<Long> basketIds;
}
