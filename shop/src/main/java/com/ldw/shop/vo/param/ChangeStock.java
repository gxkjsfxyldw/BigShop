package com.ldw.shop.vo.param;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("订单商品prod和sku库存")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeStock {

    private List<GoodsChange> prodChangeList;

    private List<SkuChange> skuChangeList;
}
