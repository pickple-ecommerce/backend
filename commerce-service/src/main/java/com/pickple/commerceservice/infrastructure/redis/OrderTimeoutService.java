package com.pickple.commerceservice.infrastructure.redis;

import com.pickple.commerceservice.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTimeoutService {

    private final OrderService orderService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 5분마다 체크
    @Scheduled(fixedRate = 30000)
    public void checkPaymentTimeout() {
        Set<String> keys = redisTemplate.keys("*");  // 모든 Redis 키 가져오기
        if (keys != null) {
            for (String key : keys) {
                if (key == null) {
                    continue; // key가 null인 경우 건너뛰기
                }

                try {
                    UUID orderId = UUID.fromString(key);  // UUID 형식으로 변환
                    // 키가 존재하는지 확인
                    if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                        // 키가 만료되었다면 결제 취소 및 주문 취소 처리
                        orderService.handleOrderTimeout(orderId);
                    }
                } catch (IllegalArgumentException e) {
                    // UUID 형식이 아닌 경우 건너뛰기
                }
            }
        }
    }
}