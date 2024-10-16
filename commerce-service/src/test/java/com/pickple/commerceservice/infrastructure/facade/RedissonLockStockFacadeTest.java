package com.pickple.commerceservice.infrastructure.facade;

import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonLockStockFacadeTest {
    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private RedissonClient redissonClient;

    private Product product;
    private Stock stock;
    private final UUID productId = UUID.fromString("4c3ad20e-5117-4ec6-a41f-53e00a7c117b");
    private final UUID stockId = UUID.fromString("a7af4401-ce29-4fab-b1f1-e1906abcb07a");
    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId(productId)
                .productName("test 상품")
                .build();

        stock = Stock.builder()
                .stockId(stockId)
                .stockQuantity(50L) // 초기 재고를 100개로 설정
                .product(product)
                .build();
        stockRepository.saveAndFlush(stock); // 재고 저장
    }

    @Test
    @DisplayName("동시에 100개의 요청에서 재고 감소 테스트")
    void decreaseStockQuantityWithLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 100개의 스레드를 사용해 동시 재고 감소 요청을 보냄
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decreaseStockQuantityWithLock(productId); // 재고 감소 요청
                } finally {
                    latch.countDown(); // 요청이 끝날 때마다 카운트다운
                }
            });
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // Then (재고 차감 검증)
        Optional<Stock> updatedStock = stockRepository.findById(stockId);
        assertEquals(0L, updatedStock.orElseThrow().getStockQuantity()); // 재고가 50개에서 0개로 정확히 차감되었는지 확인
    }


}