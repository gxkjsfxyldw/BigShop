package com.ldw.shop.vo.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "User用户对象",description = "提供用户能修改的字段")
public class UserVo {

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 头像图片路径
     */
    @TableField(value = "user_face")
    private String userFace;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_mail")
    private String userMail;


    /**
     * M(男) or F(女)
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 例如：2009-11-27
     */
    @TableField(value = "birth_date")
    private String birthDate;

    /**
     * 用户手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    private String payPassword;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
