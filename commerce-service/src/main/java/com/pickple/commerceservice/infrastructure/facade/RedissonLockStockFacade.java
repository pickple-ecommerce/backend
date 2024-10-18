package com.pickple.commerceservice.infrastructure.facade;

import com.pickple.commerceservice.application.service.StockRetryService;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.common_module.exception.CustomException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockRetryService stockRetryService;

    /**
     * 주어진 productId에 대해 재고를 감소시키는 메서드
     * @param productId 재고를 감소시킬 상품 ID
     */
    public void decreaseStockQuantityWithLock(UUID productId) {
        RLock lock = redissonClient.getLock("stockLock:" + productId);

        try {
            boolean isLocked = lock.tryLock(15, 1, TimeUnit.SECONDS);
            if (!isLocked) {
                log.error("재고 lock 획득 실패. 상품 ID: " + productId);
                throw new CustomException(CommerceErrorCode.STOCK_LOCK_FAILED);
            }

            // 락을 성공적으로 획득한 경우 재고 감소 메서드 호출
            stockRetryService.decreaseStockQuantityWithRetry(productId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("재고 감소 작업이 중단되었습니다: " + e.getMessage());
        } finally {
            // 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
