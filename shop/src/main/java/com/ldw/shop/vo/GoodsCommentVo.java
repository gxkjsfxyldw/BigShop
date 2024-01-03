package com.ldw.shop.vo;

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
@ApiModel("商品评论总览")
public class GoodsCommentVo {

    @ApiModelProperty("好评率")
    private BigDecimal positiveRating;

    @ApiModelProperty("评论总数量")
    private Integer number;

    @ApiModelProperty("好评数量")
    private Integer praiseNumber;

    @ApiModelProperty("中评数量")
    private Integer secondaryNumber;

    @ApiModelProperty("差评数量")
    private Integer negativeNumber;

    @ApiModelProperty("有图评论数量")
    private Integer picNumber;

}
