package com.ldw.shop.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "goods")
public class Goods  implements Serializable {

    /**
     * 产品ID
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    @TableField(value = "brief")
    private String brief;

    /**
     * 详细描述
     */
    @TableField(value = "content")
    private String content;

    /**
     * 商品主图
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 商品图片，以,分割
     */
    @TableField(value = "imgs")
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 商品分类
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 销量
     */
    @TableField(value = "sold_num")
    private Integer soldNum;
    /**
     * 商品浏览量
     */
    @TableField(value = "view_counts")
    private Integer viewCounts;
    /**
     * 总库存
     */
    @TableField(value = "total_stocks")
    private Integer totalStocks;

    /**
     * 配送方式json见TransportModeVO
     */
    @TableField(value = "delivery_mode")
    private String deliveryMode;

    /**
     * 运费模板id
     */
    @TableField(value = "delivery_template_id")
    private Long deliveryTemplateId;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 上架时间
     */
    @TableField(value = "putaway_time")
    private Date putawayTime;

    /**
     * 版本 乐观锁
     */
    @TableField(value = "version")
    private Integer version;

    //////////////////新增商品////////////////////
    @TableField(exist = false)
    private List<Sku> skuList;

    @TableField(exist = false)
    private List<Long> tagList;

    @TableField(exist = false)
    private DeliveryModeVo deliveryModeVo;

    @Data
    @ApiModel("配送方式")
    public static class DeliveryModeVo{

        @TableField(exist = false)
        @ApiModelProperty("商品配送")
        private Boolean hasShopDelivery;

        @TableField(exist = false)
        @ApiModelProperty("用户自提")
        private Boolean hasUserPickUp;
    }

    private static final long serialVersionUID = 1L;
}
