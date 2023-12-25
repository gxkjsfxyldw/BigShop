package com.ldw.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//创建线程池，为了更新商品查看数量而不影响查看商品详情的主线程
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("taskExecutor")//bean的名称
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor excutor = new ThreadPoolTaskExecutor();
        //设置线程核心数量
        excutor.setCorePoolSize(20);
        //设置线程最大数量
        excutor.setMaxPoolSize(50);
        //配置队列大小
        excutor.setQueueCapacity(Integer.MAX_VALUE);
        //设置线程活跃时间（秒）
        excutor.setKeepAliveSeconds(60);
        //设置默认线程名称
        excutor.setThreadNamePrefix("ldw_BigShop");
        //等待所有任务结束后关闭线程池
        excutor.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        excutor.initialize();
        return excutor;
    }
}
