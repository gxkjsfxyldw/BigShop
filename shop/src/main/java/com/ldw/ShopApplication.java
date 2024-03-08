package com.ldw;

import cn.hutool.core.lang.Snowflake;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ldw.shop.dao.mapper")
@EnableScheduling
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
    //雪花算法
    @Bean
    public Snowflake snowflake() {
        return new Snowflake(0L,0L);
    }

//    @Bean
//    public Redisson redisson(){
//        //单机模式
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
//        config.setLockWatchdogTimeout(10000);//设置分布式锁watch dog超时时间
//        return (Redisson)Redisson.create(config);
//    }

}
