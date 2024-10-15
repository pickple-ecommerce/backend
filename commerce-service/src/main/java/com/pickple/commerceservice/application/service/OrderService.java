package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.application.dto.OrderDetailResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.facade.RedissonLockStockFacade;
import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockService stockService;
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, String username) {
        // 주문 정보 생성
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .username(username)
                .build();

        // OrderDetail 정보 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
                    BigDecimal unitPrice = product.getProductPrice();
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .unitPrice(unitPrice)
                            .build();
                    orderDetail.calculateTotalPrice(); // 단가*수량 계산
                    return orderDetail;
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        orderRepository.save(order);

        // 결제 요청 (kafka)
        messagingProducerService.sendPaymentRequest(
                order.getOrderId(),
                order.getAmount(),
                username
        );

        // 배송 정보 저장 (redis)
        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

        // OrderDetail Dto로 변환
        List<OrderDetailResponseDto> orderDetailDtos = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getProductId())
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .username(username)
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetailDtos)
                .build();
    }

    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId, String role, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        PaymentClientDto paymentInfo = paymentClient.getPaymentInfo(role, username, orderId);
        DeliveryClientDto deliveryInfo = deliveryClient.getDeliveryInfo(role, username, orderId);

        List<OrderDetailResponseDto> orderDetailDtos = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getProductId())
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetailDtos)
                .paymentInfo(paymentInfo)
                .deliveryInfo(deliveryInfo)
                .build();
    }

    /**
     * 예약 구매 주문 생성
     */
    @Transactional
    public void createPreOrder(PreOrderRequestDto requestDto, String username) {
        // 상품 조회
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));

        // 재고 확인 및 차감
        try {
            redissonLockStockFacade.decreaseStockQuantityWithLock(product.getProductId());
        } catch (CustomException e) {
            log.error("주문 생성 실패: 재고가 부족합니다.");
            throw e;
        }

        // 주문 및 주문 정보 생성
        Order order = Order.builder()
                .username(username)
                .amount(product.getProductPrice())     // 주문 총액은 곧 상품 가격
                .build();
        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .unitPrice(product.getProductPrice())
                .orderQuantity(1L)                     // 항상 수량은 1개
                .totalPrice(product.getProductPrice()) // 총 가격을 바로 설정
                .order(order)
                .build();

        // 주문 상태 변경
        order.changeStatus(OrderStatus.COMPLETED);

        // 주문 저장
        order.addOrderDetails(Collections.singletonList(orderDetail));
        orderRepository.save(order);
    }

    /**
     * 주문 취소 메소드
     */
    @Transactional
    public OrderResponseDto cancelOrder(UUID orderId, String username, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        order.changeStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // 결제 취소 요청 전송 (Kafka)
        messagingProducerService.sendPaymentCancelRequest(orderId);

        // 배송 정보 조회 및 삭제 요청 처리
        try {
            DeliveryClientDto deliveryInfo = deliveryClient.getDeliveryInfo(role, username, orderId);
            messagingProducerService.sendDeliveryDeleteRequest(deliveryInfo.getDeliveryId(), orderId);
        } catch (Exception e) {
            log.warn("배송 정보 조회 실패: orderId={}, message: {}", orderId, e.getMessage());
        }

//        // 재고 롤백
//        if (order != null) {
//            rollbackStock(order);
//        }

        log.info("주문 취소 완료: orderId={}, username={}", orderId, username);

        return mapToOrderResponseDto(order);
    }

    /**
     * 주문을 OrderResponseDto로 매핑하는 메서드
     */
    private OrderResponseDto mapToOrderResponseDto(Order order) {
        List<OrderDetailResponseDto> orderDetails = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getProductId())
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetails)
                .build();
    }

}