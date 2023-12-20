package com.ldw.shop.config;


import com.ldw.shop.handle.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override  //跨域配置
    public void addCorsMappings(CorsRegistry registry) {
        //允许浏览器的8080端口访问后端的6060端口
        registry.addMapping("/**") // 所有接口
                .allowedOriginPatterns("http://localhost:8080") // 支持域
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 支持方法
                .allowedHeaders("*")
                .exposedHeaders("*");
//        registry.addMapping("/**").allowedOrigins("服务器ip地址");
    }

    //只有经过登录拦截器才可获取当前登录用户的信息
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //定义 放行路径
        String[] swaggerExcludes = new String[]{
                "/login","/swagger**/**","/webjars/**","/v3/**","/doc.html"
        };

        //拦截test接口，后续遇到实际需要拦截的接口时，在进行添加拦截  未登录不给它进入首页即可
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") //拦截全部请求
                .excludePathPatterns(swaggerExcludes);//放行白名单
    }
    //springboot2.x 静态资源在自定义拦截器之后无法访问的解决方案
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}

