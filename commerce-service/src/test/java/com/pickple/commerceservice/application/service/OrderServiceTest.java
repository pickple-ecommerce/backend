package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.facade.RedissonLockStockFacade;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockService stockService;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;
    @Mock
    private RedissonLockStockFacade redissonLockStockFacade;
    private Product product;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        // 상품 및 재고 설정
        product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("테스트 상품")
                .productPrice(BigDecimal.valueOf(1000))
                .build();

        stock = Stock.builder()
                .stockId(UUID.randomUUID())
                .stockQuantity(50L)  // 초기 재고 50개
                .product(product)
                .build();

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
    }

    @Test
    @DisplayName("동시에 100개의 주문 생성 요청 테스트")
    @Disabled
    void createPreOrder_MultiThreaded() throws InterruptedException {
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 10개의 스레드 풀 사용

        PreOrderRequestDto requestDto = new PreOrderRequestDto(product.getProductId());

        // 재고가 충분할 때는 정상적으로 재고 차감이 발생하도록 설정
        AtomicInteger stockCounter = new AtomicInteger(50);  // 남은 재고 추적

        doAnswer(invocation -> {
            int currentStock = stockCounter.get();
            if (currentStock > 0) {
                stockCounter.decrementAndGet();  // 재고 차감
            } else {
                throw new CustomException(CommerceErrorCode.INSUFFICIENT_STOCK);  // 재고 부족 예외 발생
            }
            return null;
        }).when(redissonLockStockFacade).decreaseStockQuantityWithLock(any(UUID.class));

        AtomicInteger orderCount = new AtomicInteger(0);

        // OrderRepository의 save 메서드를 호출할 때마다 orderCount 증가
        doAnswer(invocation -> {
            orderCount.incrementAndGet();
            return null;
        }).when(orderRepository).save(any(Order.class));

        // 멀티 스레딩으로 100개의 주문 생성 시도
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.createPreOrder(requestDto, "test-user");
                } catch (CustomException e) {
                    // 재고 부족으로 인한 CustomException 발생 시 처리
                    assertEquals(CommerceErrorCode.INSUFFICIENT_STOCK, e.getErrorCode());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // 재고 0개로 감소한 것을 확인
        assertEquals(0, stockCounter.get());
        // 성공적으로 주문이 50개만 생성된 것을 확인
        assertEquals(50, orderCount.get());
        // orderRepository.save()가 정확히 50번 호출되었는지 확인
        verify(orderRepository, times(50)).save(any(Order.class));
    }

    @Test
    @DisplayName("정상적인 예약 구매 주문 생성 테스트")
    @Disabled
    void createPreOrder() {
        // Given: 테스트에 필요한 데이터 설정
        UUID productId = UUID.randomUUID();
        String username = "testUser";

        Product product = Product.builder()
                .productId(productId)
                .productName("테스트 상품")
                .productPrice(BigDecimal.valueOf(1000))
                .build();

        PreOrderRequestDto requestDto = PreOrderRequestDto.builder()
                .productId(productId)
                .build();

        // Mock의 동작 설정
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(stockService).decreaseStockQuantity(productId);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: 서비스 메서드 호출
        orderService.createPreOrder(requestDto, username);

        // Then: 검증
        verify(productRepository, times(1)).findById(productId);  // 상품 조회 호출 1회
        verify(stockService, times(1)).decreaseStockQuantity(productId);  // 재고 차감 호출 1회
        verify(orderRepository, times(1)).save(any(Order.class));  // 주문 저장 호출 1회

        // 저장된 주문 객체의 상태 검증
        verify(orderRepository).save(argThat(order ->
                order.getUsername().equals(username) &&
                        order.getAmount().equals(product.getProductPrice()) &&
                        order.getOrderStatus() == OrderStatus.COMPLETED
        ));
    }

    @Test
    @DisplayName("상품을 찾을 수 없을 때 예외 발생 테스트")
    void createPreOrder_ProductNotFound() {
        // Given: 존재하지 않는 상품 ID
        UUID productId = UUID.randomUUID();
        String username = "testUser";

        PreOrderRequestDto requestDto = PreOrderRequestDto.builder()
                .productId(productId)
                .build();

        // Mock의 동작 설정
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then: 예외 발생 검증
        CustomException exception = assertThrows(CustomException.class, () ->
                orderService.createPreOrder(requestDto, username)
        );

        // 예외 메시지가 올바르게 설정되었는지 확인
        assertEquals(CommerceErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());

        // 상품 조회 호출 확인
        verify(productRepository, times(1)).findById(productId);

        // 재고 차감 및 주문 저장이 호출되지 않았는지 확인
        verify(stockService, never()).decreaseStockQuantity(any());
        verify(orderRepository, never()).save(any());
    }
}