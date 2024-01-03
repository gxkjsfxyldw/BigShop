package com.ldw.shop.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品评论
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "goods_comment")
public class GoodsComment implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 订单项ID
     */
    @TableField(value = "order_item_id")
    private Long orderItemId;

    /**
     * 评论用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 掌柜回复
     */
    @TableField(value = "reply_content")
    private String replyContent;

    /**
     * 记录时间
     */
    @TableField(value = "rec_time")
    private Date recTime;

    /**
     * 回复时间
     */
    @TableField(value = "reply_time")
    private Date replyTime;

    /**
     * 是否回复 0:未回复  1:已回复
     */
    @TableField(value = "reply_sts")
    private Integer replySts;

    /**
     * IP来源
     */
    @TableField(value = "postip")
    private String postip;

    /**
     * 得分，0-5分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 有用的计数
     */
    @TableField(value = "useful_counts")
    private Integer usefulCounts;

    /**
     * 晒图的json字符串
     */
    @TableField(value = "pics")
    private String pics;

    /**
     * 是否匿名(1:是  0:否)
     */
    @TableField(value = "is_anonymous")
    private Integer isAnonymous;

    /**
     * 是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 评价(0好评 1中评 2差评)
     */
    @TableField(value = "evaluate")
    private Integer evaluate;

    ///////////////分页查询////////////////
    @TableField(exist = false)
    private String goodsName;


    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String pic;


    private static final long serialVersionUID = 1L;

///////////////////////////////////////////////////

    @TableField(exist = false)
    @ApiModelProperty("好评率")
    private BigDecimal positiveRating;

    @TableField(exist = false)
    @ApiModelProperty("评论总数量")
    private Integer number;

    @TableField(exist = false)
    @ApiModelProperty("好评数量")
    private Integer praiseNumber;

    @TableField(exist = false)
    @ApiModelProperty("中评数量")
    private Integer secondaryNumber;

    @TableField(exist = false)
    @ApiModelProperty("差评数量")
    private Integer negativeNumber;

    @TableField(exist = false)
    @ApiModelProperty("有图评论数量")
    private Integer picNumber;

}
