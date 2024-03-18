package com.ldw.shop.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ApiCallAdvice {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HttpServletRequest request;
    private static final String FORMAT_PATTERN_DAY="yyyy-MM-dd";

    //配置织入点
    @Pointcut("@annotation(com.ldw.shop.common.aop.ApiCall)")
    public void apiCall(){

    }

    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint joinPoint, ApiCall rateLimit) throws Throwable {

        String ipAddress = getClientIpAddress(request);
        String key = "rate_limit:" + ipAddress;

        // 从Redis中获取请求次数
        Long count = redisTemplate.opsForValue().increment(key, 1);

        // 如果是第一次请求，设置过期时间
        if (count != null && count == 1) {
            redisTemplate.expire(key, rateLimit.time(), rateLimit.timeUnit());
        }

        // 判断请求次数是否超出限制
        if (count != null && count > rateLimit.limit()) {
            throw new RuntimeException("请求太频繁，请稍后再试！");
        }

        // 执行目标方法
        return joinPoint.proceed();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0];
    }

    /**
     * 真正执行业务操作前，先进行总的限流验证
     * 限制维度为：一天内单个IP的访问次数
     * key = URL+IP+date(精确到天)
     * value = 调用次数
     */
    @Before("apiCall()")
    public void before(){

        //接受到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip = getClientIpAddress(request);
        String url = request.getRequestURI();
        String date = new SimpleDateFormat(FORMAT_PATTERN_DAY).format(new Date());
        String key = "rate_limit:" + url + ":" + ip + ":" + date;

        if(redisTemplate.hasKey(key)){
            int count= Integer.parseInt(redisTemplate.opsForValue().get(key));
            //判断这个key是否超过1千，一天之内只允许1千次访问
            if(count>5){
                throw new RuntimeException("当天请求已达上限，请明日再试！");
            }
            redisTemplate.opsForValue().increment(key,1);
        }else{
            redisTemplate.opsForValue().set(key, String.valueOf(1),1L,TimeUnit.DAYS);
        }
    }


}
