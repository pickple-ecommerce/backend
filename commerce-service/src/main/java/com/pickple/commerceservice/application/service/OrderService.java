package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.response.OrderCreateResponseDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, String username) {
        // 주문 정보 생성
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .username(username)
                .build();

        // 주문 세부 정보 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> OrderDetail.builder()
                        .order(order)
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails); // Order에 OrderDetail 추가

        // 주문 세부 사항의 금액을 합산하여 총 금액을 설정
        order.calculateTotalAmount();

        orderRepository.save(order);

        // 결제 정보 Kafka 메시지 전송
        messagingProducerService.sendPaymentRequest(
                order.getOrderId(),
                order.getAmount(),
                username
        );

        // 배송 정보 임시 저장 (Redis)
        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .build();
    }

    public void handlePaymentComplete(UUID orderId, String username) {
        // 결제 완료 메시지가 오면 호출되는 메서드로, 배송 생성 메시지를 보냄
        messagingProducerService.sendDeliveryCreateRequest(orderId, username);
    }
}