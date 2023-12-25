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
import java.util.Date;

/**
 * 用户配送地址表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_address")
public class UserAddress implements Serializable {
    /**
     * ID
     */
    @TableId(value = "addr_id", type = IdType.AUTO)
    private Long addrId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 收货人
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 省ID
     */
    @TableField(value = "province_id")
    private Long provinceId;

    /**
     * 省
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 城市ID
     */
    @TableField(value = "city_id")
    private Long cityId;

    /**
     * 区
     */
    @TableField(value = "area")
    private String area;

    /**
     * 区ID
     */
    @TableField(value = "area_id")
    private Long areaId;

    /**
     * 邮编
     */
    @TableField(value = "post_code")
    private String postCode;

    /**
     * 地址
     */
    @TableField(value = "addr")
    private String addr;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 状态,1正常，0无效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否默认地址 1是
     */
    @TableField(value = "common_addr")
    private Integer commonAddr;

    /**
     * 建立时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
