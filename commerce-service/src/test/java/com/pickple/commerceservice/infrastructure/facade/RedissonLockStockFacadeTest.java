package com.pickple.commerceservice.infrastructure.facade;

import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import org.junit.jupiter.api.*;
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
    private final UUID productId = UUID.fromString("33bc4ae0-4b25-4b21-bded-bf45820419cc");
    private final UUID stockId = UUID.fromString("92984725-5244-4897-a3c6-6563ccdbb1ae");
    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId(productId)
                .productName("test 상품")
                .build();

        stock = Stock.builder()
                .stockId(stockId)
                .stockQuantity(50L) // 초기 재고를 50개로 설정
                .product(product)
                .build();
        stockRepository.saveAndFlush(stock); // 재고 저장
    }

    @Test
    @Disabled("테스트 성공 확인 후 비활성")
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