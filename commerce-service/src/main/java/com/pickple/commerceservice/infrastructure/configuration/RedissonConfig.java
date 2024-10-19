package com.pickple.commerceservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // Redis 서버 주소 설정
        config.useSingleServer().setAddress("redis://" + redisHost + ":6379");

        // ObjectMapper를 설정하고 Redisson에서 사용할 수 있도록 JsonJacksonCodec으로 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // LocalDateTime 같은 모듈 등록
        objectMapper.deactivateDefaultTyping(); // @class 필드를 비활성화

        config.setCodec(new JsonJacksonCodec(objectMapper));

        return Redisson.create(config);
    }
}
