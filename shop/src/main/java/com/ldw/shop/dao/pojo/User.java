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
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "User")
public class User implements Serializable {

    /**
     *
     * 用户表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    @TableField(value = "username")
    private String username;

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
    @TableField(value = "user_mail")
    private String phone;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    private String payPassword;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    private Date userLasttime;
    /**
     * 修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    /**
     * 注册时间
     */
    @TableField(value = "user_regtime")
    private Date userRegtime;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}
