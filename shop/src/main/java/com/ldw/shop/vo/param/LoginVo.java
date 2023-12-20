package com.ldw.shop.vo.param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)//注解会生成equals (Object other)和hashCode ()方法
@Accessors(chain = true) //对应字段的setter方法调用后，会返回当前对象
@ApiModel(value = "LoginVo登录对象",description = "")
public class LoginVo {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
}
