package com.example.ruiji_takeout.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        FastJsonRedisSerializer<Object> valueSerializer = new FastJsonRedisSerializer<>(Object.class);


        // key和value的序列化
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);

        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
