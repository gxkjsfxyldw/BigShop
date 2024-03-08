package com.ldw.shop.config;


import com.ldw.shop.common.constant.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderQueueConfig {

    /**
     * 创建延迟队列
     * @return
     */
    @Bean
    public Queue orderMsQueue() {

        Map<String,Object> map = new HashMap<>();
        //配置消息存活的时候
        map.put("x-message-ttl",60*1000);
        //配置消息死后去哪个交换机
        map.put("x-dead-letter-exchange",QueueConstant.ORDER_DEAD_EX);
        //配置消息死后去哪个路由key
        map.put("x-dead-letter-routing-key",QueueConstant.ORDER_DEAD_KEY);
        // 1、队列的名称
        // 2、是否持久化
        // 3、是否排外的
        // 4、是否自动删除
        // 5、设置队列的其他一些参数
        return new Queue(QueueConstant.ORDER_MS_QUEUE,true,false,false,map);
    }

    //队列
    @Bean
    public Queue orderDeadQueue() {
        return new Queue(QueueConstant.ORDER_DEAD_QUEUE);
    }
    //交换机
    //直连交换机 给定一个路由key ,生产者发送消息到交换机，消费者通过监听获取消息，开发中注意key一直，否则出现不匹配异常
    @Bean
    public DirectExchange orderDeadEx() {
        return new DirectExchange(QueueConstant.ORDER_DEAD_EX);
    }

    //绑定队列和交换机
    @Bean
    public Binding orderDeadBind() {
        return BindingBuilder.bind(orderDeadQueue()).to(orderDeadEx()).with(QueueConstant.ORDER_DEAD_KEY);
    }
}
