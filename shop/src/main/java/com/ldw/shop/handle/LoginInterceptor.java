package com.ldw.shop.handle;

import com.alibaba.fastjson.JSON;
import com.ldw.shop.common.constant.ErrorCode;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.LoginService;
import com.ldw.shop.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录拦截器
@Component
@Slf4j //打印日志
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行controller方法（Handler）之前执行的操作
        /*
           1.需要判断，请求接口路径，是否为 HandlerMethod（必须是controller方法）
           2.判断token是否为空，如果为空，未登录
           3.如果token不为空，登录验证  loginService checkToken
           4.如果认证成功，放行即可
         */
        if (!(handler instanceof HandlerMethod)) { //设置一些必须放行的资源路径
            //handler 可能是资源，RequestResourceHandler springboot 程序，访问静态资源 默认去classpath下的static目录去查询
            return true;//放行
        }

        //拦截未登录 token为空
        String token = request.getHeader("Authorization");

        log.info("=======================request start=========================");
        String requestURI = request.getRequestURI();
        log.info("request: uri:{}", requestURI);
        log.info("request: method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=======================request end=========================");

        if (StringUtils.isAllBlank(token)) {
            Result fail = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //将响应体返回类型设置未json
            response.setContentType("Application/json;charset=utf-8");
            //将未登录信息返回到响应体
            response.getWriter().println(JSON.toJSONString(fail));
            return false;
        }
        //拦截token是否合法
        User sysUser = loginService.chckenToken(token);
        if (sysUser == null) {
            Result fail = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("Application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(fail));
            return false;
        }
        //登录验证完成，放行
        //如果想在controller中，直接获取用户的信息，如何获取？
        UserThreadLocal.put(sysUser);//将登录用户信息放入
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除 ThreadLocal 中用完的信息，会有内存泄露的危险
        UserThreadLocal.remove();
    }
}
