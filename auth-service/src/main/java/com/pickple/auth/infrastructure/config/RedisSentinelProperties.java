package com.pickple.auth.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis.sentinel")
public class RedisSentinelProperties {
    private String master;
    private List<String> nodes;

    @Data
    public static class Master {
        private String host;
        private Integer port;
    }

    @Data
    public static class Slave {
        private String host;
        private Integer port;
    }
}
