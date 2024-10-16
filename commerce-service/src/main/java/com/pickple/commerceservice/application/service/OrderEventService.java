package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderRepository orderRepository;
    private final OrderMessagingProducerService messagingProducerService;

    /**
     * 결제 완료 처리 후 배송 요청을 보내는 메서드
     */
    @Transactional
    public void handlePaymentComplete(UUID orderId, UUID paymentId, String username) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
                CommerceErrorCode.ORDER_NOT_FOUND));
        order.assignPaymentId(paymentId);
        orderRepository.save(order); // order 조회 test 목적
        // 결제 완료 메시지가 오면 호출되는 메서드로, 배송 생성 메시지를 보냄
        messagingProducerService.sendDeliveryCreateRequest(orderId, username);
    }

    /**
     * 배송 생성 완료 처리 메서드
     */
    @Transactional
    public void handleDeliveryCreate(UUID orderId, UUID deliveryId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
                CommerceErrorCode.ORDER_NOT_FOUND));
        order.assignDeliveryId(deliveryId);
        orderRepository.save(order); // order 조회 test 목적
    }

    /**
     * 배송 완료 시 주문 상태를 COMPLETED로 변경하는 메서드
     */
    @Transactional
    public void handleDeliveryEnd(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 주문 상태를 COMPLETED로 변경
        order.changeStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }
}
