package com.ldw.shop.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override  //跨域配置
    public void addCorsMappings(CorsRegistry registry) {
        //允许浏览器的8080端口访问后端的8888端口
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");

    }

    //只有经过登录拦截器才可获取当前登录用户的信息
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口，后续遇到实际需要拦截的接口时，在进行添加拦截  未登录不给它进入首页即可
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") //拦截全部请求
                .excludePathPatterns("/login");//过滤登录接口
    }
}

