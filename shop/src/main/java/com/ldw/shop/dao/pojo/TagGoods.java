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
 * 商品分组表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tag_goods")
public class TagGoods  implements Serializable {
    /**
     * 分组标签id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 店铺Id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 状态(1为正常,0为删除)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 默认类型(0:商家自定义,1:系统默认)
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     * 商品数量
     */
    @TableField(value = "prod_count")
    private Long prodCount;

    /**
     * 列表样式(0:一列一个,1:一列两个,2:一列三个)
     */
    @TableField(value = "style")
    private Integer style;

    /**
     * 排序
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除时间
     */
    @TableField(value = "delete_time")
    private Date deleteTime;

    private static final long serialVersionUID = 1L;
}
