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
 * 标签与商品关联表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tag_goods_reference")
public class TagGoodsReference implements Serializable {
    /**
     * 分组引用id
     */
    @TableId(value = "reference_id", type = IdType.AUTO)
    private Long referenceId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Long tagId;

    /**
     * 商品id
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 状态(1:正常,0:删除)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
