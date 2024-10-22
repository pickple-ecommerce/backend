package com.pickple.gateway.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis.sentinel")
public class RedisSentinelProperties {
    private RedisMasterProperties master;
    private List<String> nodes;

    @Data
    public static class RedisMasterProperties {
        private String host;
        private Integer port;
    }

}