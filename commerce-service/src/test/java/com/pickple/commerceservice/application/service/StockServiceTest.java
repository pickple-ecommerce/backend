package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.common_module.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @InjectMocks
    private StockService stockService;
    private Stock stock;
    private Product product;
    private OrderDetail orderDetail;

    @BeforeEach
    void setUp() {

        // 상품과 재고 초기화
        product = Product.builder()
                .productId(UUID.randomUUID())
                .build();

        stock = Stock.builder()
                .stockId(UUID.randomUUID())
                .stockQuantity(100L)
                .build();

        orderDetail = OrderDetail.builder()
                .orderQuantity(2L) // 주문 수량
                .product(product)
                .build();
    }

    @Test
    @DisplayName("재고가 1씩 증가")
    void testIncreaseStockQuantity() {
        UUID productId = stock.getStockId();
        when(stockRepository.findByProduct_ProductId(productId)).thenReturn(Optional.of(stock));

        stockService.increaseStockQuantity(productId);

        verify(stockRepository).findByProduct_ProductId(productId);
        assertEquals(101L, stock.getStockQuantity()); // 수량 증가 확인
    }

    @Test
    @DisplayName("재고가 1씩 감소")
    void testDecreaseStockQuantity() {
        UUID productId = stock.getStockId();
        when(stockRepository.findByProduct_ProductId(productId)).thenReturn(Optional.of(stock));

        stockService.decreaseStockQuantity(productId);

        verify(stockRepository).findByProduct_ProductId(productId);
        assertEquals(99L, stock.getStockQuantity()); // 수량 감소 확인
    }


    @Test
    @DisplayName("동시에 100개의 요청에서 재고 감소 테스트")
    @Disabled("이 테스트 코드는 RedissonLockStockFacadeTest에서 구현됐으므로 비활성됐습니다.")
    void testDecreaseStockQuantityConcurrency() throws InterruptedException {
        // Given
        UUID productId = stock.getStockId();
        when(stockRepository.findByProduct_ProductId(productId)).thenReturn(Optional.of(stock));

        // 100개의 요청을 처리할 스레드 풀과 카운트다운 래치 설정
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseStockQuantity(productId); // 재고 감소 요청
                } finally {
                    latch.countDown(); // 요청이 끝날 때마다 카운트다운
                }
            });
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // Then (재고 차감 검증)
        assertEquals(0L, stock.getStockQuantity()); // 재고가 100개에서 0개로 정확히 차감되었는지 확인
        verify(stockRepository, times(threadCount)).findByProduct_ProductId(productId); // 100번 호출되었는지 확인
    }

    @Test
    @DisplayName("주문 수량이 충분할 때 재고 수량 감소")
    void testDecreaseStockQuantityForOrder_Success() {
        // Given: StockRepository가 재고를 반환하도록 설정
        when(stockRepository.findByProduct_ProductId(any(UUID.class))).thenReturn(Optional.of(stock));

        // When: 주문 수량만큼 재고를 감소시킴
        stockService.decreaseStockQuantityForOrder(orderDetail);

        // Then: 재고 수량이 줄어들었는지 확인
        assertEquals(98L, stock.getStockQuantity());
        verify(stockRepository, times(1)).findByProduct_ProductId(any(UUID.class));
    }
    @Test
    @DisplayName("주문 수량이 부족할 때 예외 발생")
    void testDecreaseStockQuantity_InsufficientStock() {
        // Given: 주문 수량이 현재 재고 수량보다 많음
        OrderDetail orderDetailWithExcessQuantity = OrderDetail.builder()
                .orderQuantity(115L) // 주문 수량을 15로 설정
                .product(product)
                .build();
        when(stockRepository.findByProduct_ProductId(any(UUID.class))).thenReturn(Optional.of(stock));

        // When: 재고 감소 시도 및 예외 발생 확인
        CustomException thrown = assertThrows(CustomException.class, () -> {
            stockService.decreaseStockQuantityForOrder(orderDetailWithExcessQuantity);
        });

        // Then: 예외 메시지가 올바른지 확인
        assertEquals(CommerceErrorCode.INSUFFICIENT_STOCK, thrown.getErrorCode());
        verify(stockRepository, times(1)).findByProduct_ProductId(any(UUID.class));
    }
}
