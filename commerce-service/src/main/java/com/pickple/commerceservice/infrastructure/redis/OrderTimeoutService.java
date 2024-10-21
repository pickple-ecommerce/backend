package com.pickple.commerceservice.infrastructure.redis;

import com.pickple.commerceservice.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Slf4j
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
                    // key가 null인 경우 건너뛰기
                    log.warn("null 키를 발견했습니다. 건너뜁니다.");
                    continue;
                }

                if (isValidUUID(key)) {  // UUID 형식인지 확인
                    try {
                        UUID orderId = UUID.fromString(key);

                        // 키가 존재하는지 확인
                        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                            log.info("만료된 Redis 키 발견: {}, 주문 ID: {}", key, orderId);
                            // 키가 만료되었다면 결제 취소 및 주문 취소 처리
                            orderService.handleOrderTimeout(orderId);
                        }
                    } catch (IllegalArgumentException e) {
                        log.error("유효하지 않은 UUID 형식: {}", key, e);
                    }
                } else {
                    log.info("UUID 형식이 아닌 Redis 키: {}", key);
                }
            }
        } else {
            log.info("Redis에서 키를 찾을 수 없습니다.");
        }
    }

    // UUID 형식인지 검증하는 메서드
    private boolean isValidUUID(String key) {
        try {
            UUID.fromString(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}