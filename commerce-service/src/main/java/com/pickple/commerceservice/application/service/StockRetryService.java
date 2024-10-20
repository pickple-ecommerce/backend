package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.common_module.exception.CustomException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockRetryService {

    private final StockRepository stockRepository;

    // 예약 구매: 재고 1 감소 메서드
    @Retryable(
            retryFor = {StaleObjectStateException.class, OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 500,
            backoff = @Backoff(100)
    )
    @Transactional
    public void decreaseStockQuantityWithRetry(UUID productId) {
        Stock stock = stockRepository.findByProduct_ProductIdWithLock(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.STOCK_DATA_NOT_FOUND_FOR_PRODUCT));
        stock.decreaseStock();  // 수량 1 감소
        stockRepository.save(stock);
    }

}
