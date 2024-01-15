package com.ldw.shop.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类,序列化
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object>redisTemplate(RedisConnectionFactory connectionFactory){

        //通常为了开发方便，使用<String,Object>
        RedisTemplate<String,Object>template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

//     配置具体的序列化方式
        //json序列化配置：使用json解析对象，将传入的对象编程json的序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer(Object.class);
//        转译
        ObjectMapper om =new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);


        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //String类型key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //String类型value序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //Hash类型key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash类型value序列器
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
