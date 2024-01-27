package com.ldw.shop.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 满减活动表
 * @author 26327
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("discount")
public class Discount {

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠门槛金额
     */
    @TableField(value = "full_money")
    private Long full_money;

    /**
     * 积分数量
     */
    @TableField(value = "point_amount")
    private Integer pointAmount;

    /**
     *
     * 打折
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     *
     * 活动标题
     */
    @TableField(value = "title")
    private String title;

    /**
     *
     * 活动名称
     */
    @TableField(value = "name")
    private String name;

    /**
     *
     * 活动描述
     */
    @TableField(value = "description")
    private String description;
}
