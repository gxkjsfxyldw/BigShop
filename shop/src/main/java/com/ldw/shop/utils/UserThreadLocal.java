package com.ldw.shop.utils;

import com.ldw.shop.dao.pojo.User;

//使用多线存储用户当前登录用户的信息，以便在controller中也能够获取到
public class UserThreadLocal {

    private UserThreadLocal(){}
    //线程变量隔离
    private static final ThreadLocal<User> LOCAL=new ThreadLocal<>();

    public static void put(User sysUser){//放入
        LOCAL.set(sysUser);
    }
    public static User get(){//获取
        return LOCAL.get();
    }
    public static void remove(){//删除
        LOCAL.remove();
    }

}
