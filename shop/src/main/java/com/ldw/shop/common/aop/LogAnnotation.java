package com.ldw.shop.common.aop;
import java.lang.annotation.*;
//元注解，用于指定它修饰的注解可以放在Class类上面或者method方法上面
@Target({ElementType.METHOD})
//元注解，用于指定被它修饰的注解类的生存周期，即会保留到哪个阶段,运行期丢弃
@Retention(RetentionPolicy.RUNTIME)
//元注解，用于指定被它修饰的注解类将被javadoc工具提取成文档,所以注解类型信息就会被包括在生成的帮助文档中
@Documented
public @interface LogAnnotation {
    String module() default "";
    String opertion() default "";
}
