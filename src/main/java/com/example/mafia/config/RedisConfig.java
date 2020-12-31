//package com.example.mafia.config;
//
//import com.example.mafia.entity.UserEntity;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableRedisRepositories
//@RequiredArgsConstructor
//public class RedisConfig {
//    @Autowired
//    private final RedisProperties redisProperties;
//
//    Logger log = LoggerFactory.getLogger(RedisConfig.class);
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
//        redisStandaloneConfiguration.setPort(redisProperties.getPort());
//        log.info("Redis ConnectionFactory Constructed....zzzzzzzzzzzzz");
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//        return lettuceConnectionFactory;
//    }
//
//    @Bean
//    public RedisTemplate<String, UserEntity> redisTemplate() {
//        RedisTemplate<String, UserEntity> redisTemplate = new RedisTemplate<>();
//        log.info("Redis Template Constructed....zzzzzzzzzzzzz : {}", redisConnectionFactory().getConnection());
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserEntity.class));
//        return redisTemplate;
//    }
//}
