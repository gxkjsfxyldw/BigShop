package com.ldw.shop.dao.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 商店表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shop")
public class Shop {


    /**
     * 产品ID
     */
    @TableId(value = "shop_id", type = IdType.AUTO)
    private Long shopId;

    /**
     * 商品名称
     */
    @TableField(value = "shop_name")
    private String shopName;

    /**
     * 商品名称
     */
    @TableField(value = "user_id")
    private Integer userId;


    /**
     * 店铺类型
     */
    @TableField(value = "shop_type")
    private Boolean shopType;
    /**
     * 店铺简介(可修改)
     */
    @TableField(value = "intro")
    private String intro;
    /**
     * 店铺公告(可修改)
     */
    @TableField(value = "shop_notice")
    private String shopNotice;
    /**
     * 店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    @TableField(value = "shop_industry")
    private Boolean shopIndustry;
    /**
     * 店长
     */
    @TableField(value = "shop_owner")
    private String shopOwner;
    /**
     * 店铺绑定的手机(登录账号：唯一)
     */
    @TableField(value = "mobile")
    private String mobile;
    /**
     * 店铺联系电话
     */
    @TableField(value = "tel")
    private String tel;
    /**
     * 店铺所在纬度(可修改)
     */
    @TableField(value = "shop_lat")
    private String shopLat;
    /**
     *店铺所在经度(可修改)
     */
    @TableField(value = "shop_lng")
    private String shopLng;
    /**
     * 店铺详细地址
     */
    @TableField(value = "shop_address")
    private String shopAddress;
    /**
     * 店铺所在省份（描述）
     */
    @TableField(value = "province")
    private String province;
    /**
     * 店铺所在城市（描述）
     */
    @TableField(value = "city")
    private String city;
    /**
     * 店铺所在区域（描述）
     */
    @TableField(value = "area")
    private String area;
    /**
     * 店铺省市区代码，用于回显
     */
    @TableField(value = "pca_code")
    private String pcaCode;
    /**
     * 店铺logo(可修改)
     */
    @TableField(value = "shop_logo")
    private String shopLogo;
    /**
     * 店铺相册
     */
    @TableField(value = "shop_photos")
    private String shopPhotos;
    /**
     * 每天营业时间段(可修改)
     */
    @TableField(value = "open_time")
    private String openTime;
    /**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    @TableField(value = "shop_status")
    private Boolean shopStatus;
    /**
     * 0:商家承担运费; 1:买家承担运费
     */
    @TableField(value = "transport_type")
    private Boolean transportType;
    /**
     * 固定运费
     */
    @TableField(value = "fixed_freight")
    private Boolean fixedFreight;
    /**
     * 满X包邮
     */
    @TableField(value = "full_free_shipping")
    private Boolean fullFreeShipping;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 分销开关(0:开启 1:关闭)
     */
    @TableField(value = "is_distribution")
    private Boolean isDistribution;

}
