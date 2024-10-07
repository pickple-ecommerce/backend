package com.pickple.gateway.infrastructure.config;

import com.pickple.gateway.application.dto.UserDto;
import com.pickple.gateway.application.service.RedisService;
import com.pickple.gateway.infrastructure.feign.UserServiceClient;
import com.pickple.gateway.infrastructure.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.util.Optional;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final UserServiceClient userServiceClient;

    public SecurityConfig(JwtUtil jwtUtil, UserServiceClient userServiceClient, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;
        this.redisService = redisService;
    }

    @Bean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .addFilterAt(jwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.HTTP_BASIC);

        return http.build();
    }

    public WebFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return (exchange, chain) -> {

            log.info("check filter run");

            // /auth/login/prometheus 경로는 필터를 적용하지 않음
            String path = exchange.getRequest().getURI().getPath();
            if (path.equals("/api/v1/auth/sign-up") || path.equals("/api/v1/auth/sign-in")
                    || path.equals("/actuator/prometheus")) {
                return chain.filter(exchange);
            }

            String tokenValue = jwtUtil.getTokenFromRequest(exchange.getRequest());

            if (StringUtils.hasText(tokenValue)) {
                // JWT 토큰 substring
                // tokenValue = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);

                String username = claims.getSubject();

                UserDto userDto = Optional.ofNullable(redisService.getValueAsClass("user:" + username, UserDto.class))
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

                // 사용자 정보를 새로운 헤더에 추가
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Name", userDto.getUsername())  // 사용자명 헤더 추가
                        .header("X-User-Roles", String.join(",", userDto.getRoles()))    // 권한 정보 헤더 추가
                        .build();

                // 수정된 요청으로 필터 체인 계속 처리
                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                return chain.filter(modifiedExchange);
            }
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }
}
