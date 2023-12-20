package com.ldw.shop.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ldw
 */
@Data
@AllArgsConstructor //创建构造器
public class Result {//返回值，成功返回数据，失败返回原因

    private boolean success;
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        return new Result(true,200,"成功",data);
    }
    public static Result fail(int  code,String msg){
        return new Result(false,code,msg,null);
    }

}
