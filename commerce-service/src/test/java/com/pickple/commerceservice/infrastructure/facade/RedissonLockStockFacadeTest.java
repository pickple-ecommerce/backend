package com.pickple.commerceservice.infrastructure.facade;

import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedissonLockStockFacadeTest {

    @Mock
    private RedissonClient redissonClient; // RedissonClient 목 객체
    @Mock
    private StockService stockService; // StockService 목 객체
    @Mock
    private StockRepository stockRepository;
    @InjectMocks
    private RedissonLockStockFacade redissonLockStockFacade; // Facade 객체
    private Stock stock;

    private RLock mockLock;
    private UUID productId;

    @BeforeEach
    void setUp() {
        // RLock 목 객체 생성
        mockLock = mock(RLock.class);
        productId = UUID.randomUUID();

        // 재고 객체 초기화
        stock = Stock.builder()
                .stockId(UUID.randomUUID())
                .stockQuantity(100L) // 초기 재고 설정
                .build();

        // RedissonClient의 getLock() 메서드 모의 설정
        when(redissonClient.getLock("stockLock:" + productId)).thenReturn(mockLock);
    }

    @Test
    @DisplayName("동시에 100개의 요청에서 재고 감소 테스트")
    void decreaseStockQuantityWithLock() throws InterruptedException{
        // Given
        when(mockLock.tryLock(15, 1, TimeUnit.SECONDS)).thenReturn(true); // 락 획득 성공 모의

        // StockService의 decreaseStockQuantity 메서드를 직접 처리하는 로직 추가
        doAnswer(invocation -> {
            // 재고 수량을 감소시키는 로직
            stock.updateStockQuantity(stock.getStockQuantity() - 1);
            return null;
        }).when(stockService).decreaseStockQuantity(productId);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When
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
        assertEquals(0L, stock.getStockQuantity()); // 재고가 100개에서 0개로 정확히 차감되었는지 확인
    }
}