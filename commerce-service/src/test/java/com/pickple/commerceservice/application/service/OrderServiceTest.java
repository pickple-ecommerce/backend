package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import com.pickple.common_module.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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


    @Test
    @DisplayName("정상적인 예약 구매 주문 생성 테스트")
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