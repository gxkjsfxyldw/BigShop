package com.ldw.shop.common.aop;

import com.alibaba.fastjson.JSON;
import com.ldw.shop.utils.HttpContextUtils;
import com.ldw.shop.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.spi.http.HttpContext;
import java.lang.reflect.Method;

@Component
@Aspect //切面 定义了通知和切点的关系
@Slf4j
public class LogAspect {

    //切点 定义在哪个类上进行连接通知
    @Pointcut("@annotation(com.ldw.shop.common.aop.LogAnnotation)")
    public void pt(){}

    //环绕通知  在类的前后都进行加强
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Long beginTime=System.currentTimeMillis();
        //执行方法
        Object result=joinPoint.proceed();
        //执行时常
        Long time=System.currentTimeMillis()-beginTime;
        //保存日志
        recordLog(joinPoint,time);
        return result;
    }
    private void recordLog(ProceedingJoinPoint joinPoint,Long time){
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        Method method=signature.getMethod();
        LogAnnotation logAnnotation=method.getAnnotation(LogAnnotation.class);
        log.info("=========================log start================================");
        log.info("medul:{}",logAnnotation.module());
        log.info("medul:{}",logAnnotation.opertion());

        //请求的方法名字
        String className=joinPoint.getTarget().getClass().getName();
        String methodName=signature.getName();
        log.info("request method:{}",className+"."+methodName+"()");

        //请求的参数
        Object[] args=joinPoint.getArgs();
        String params= JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        //获取request 设置ip地址
        HttpServletRequest request= HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));

        log.info("excute time:{} ms",time);
        log.info("=========================log end================================");
    }
}
