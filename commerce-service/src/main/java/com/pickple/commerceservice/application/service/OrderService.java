package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.response.OrderCreateResponseDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.presentation.dto.response.OrderDetailResponseDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, String username) {
        // 주문 정보 생성
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .username(username)
                .build();

        // OrderDetail 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
                    return OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .totalPrice(detail.getTotalPrice())
                            .build();
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        // Order 및 OrderDetail 함께 저장
        orderRepository.save(order);

        messagingProducerService.sendPaymentRequest(
                order.getOrderId(),
                order.getAmount(),
                username
        );

        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

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

    @Transactional
    public void handlePaymentComplete(UUID orderId, UUID paymentId, String username) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
                CommerceErrorCode.ORDER_NOT_FOUND));
        order.assignPaymentId(paymentId);
        orderRepository.save(order); // order 조회 test 목적
        // 결제 완료 메시지가 오면 호출되는 메서드로, 배송 생성 메시지를 보냄
        messagingProducerService.sendDeliveryCreateRequest(orderId, username);
    }

    @Transactional
    public void handleDeliveryCreate(UUID orderId, UUID deliveryId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
                CommerceErrorCode.ORDER_NOT_FOUND));
        order.assignDeliveryId(deliveryId);
        orderRepository.save(order); // order 조회 test 목적
    }
}