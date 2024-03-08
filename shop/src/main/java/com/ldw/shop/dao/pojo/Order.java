package com.ldw.shop.dao.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`order`")
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 产品名称,多个产品将会以逗号隔开
     */
    @TableField(value = "prod_name")
    private String prodName;

    /**
     * 订购用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 订购流水号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 总值
     */
    @TableField(value = "total")
    private BigDecimal total;

    /**
     * 实际总值
     */
    @TableField(value = "actual_total")
    private BigDecimal actualTotal;

    /**
     * 支付方式 0 手动代付 1 微信支付 2 支付宝
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 订单备注
     */
    @TableField(value = "remarks")
    private String remarks;

    /**
     * 订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 配送类型
     */
    @TableField(value = "dvy_type")
    private String dvyType;

    /**
     * 配送方式ID
     */
    @TableField(value = "dvy_id")
    private Long dvyId;

    /**
     * 物流单号
     */
    @TableField(value = "dvy_flow_id")
    private String dvyFlowId;

    /**
     * 订单运费
     */
    @TableField(value = "freight_amount")
    private BigDecimal freightAmount;

    /**
     * 用户订单地址Id
     */
    @TableField(value = "addr_order_id")
    private Long addrOrderId;

    /**
     * 订单商品总数
     */
    @TableField(value = "product_nums")
    private Integer productNums;

    /**
     * 订购时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 订单更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 付款时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

    /**
     * 发货时间
     */
    @TableField(value = "dvy_time")
    private Date dvyTime;

    /**
     * 完成时间
     */
    @TableField(value = "finally_time")
    private Date finallyTime;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private Date cancelTime;

    /**
     * 是否已经支付，1：已经支付过，0：，没有支付过
     */
    @TableField(value = "is_payed")
    private Integer isPayed;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    @TableField(value = "delete_status")
    private Integer deleteStatus;

    /**
     * 0:默认,1:在处理,2:处理完成
     */
    @TableField(value = "refund_sts")
    private Integer refundSts;

    /**
     * 优惠总额
     */
    @TableField(value = "reduce_amount")
    private BigDecimal reduceAmount;

    /**
     * 订单类型
     */
    @TableField(value = "order_type")
    private Integer orderType;

    /**
     * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
     */
    @TableField(value = "close_type")
    private Integer closeType;

    private static final long serialVersionUID = 1L;
}
