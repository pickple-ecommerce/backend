package com.pickple.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisSentinelProperties redisSentinelProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis Sentinel 설정
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisSentinelProperties.getMaster());

        // 노드를 Set<RedisNode>로 변환
        Set<RedisNode> nodes = new HashSet<>();
        for (String node : redisSentinelProperties.getNodes()) {
            String[] parts = node.split(":");
            nodes.add(new RedisNode(parts[0], Integer.parseInt(parts[1])));
        }

        // 각 노드를 sentinelConfig에 추가
        for (RedisNode redisNode : nodes) {
            sentinelConfig.sentinel(redisNode);
        }

        return new LettuceConnectionFactory(sentinelConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // LocalDateTime 같은 타입을 위한 모듈 자동 등록
        objectMapper.deactivateDefaultTyping(); // @class 제거

        // Jackson2JsonRedisSerializer 설정
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // 키는 String, 값은 JSON 형식으로 처리
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }
}