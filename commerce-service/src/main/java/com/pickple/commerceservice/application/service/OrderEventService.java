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
        order.changeStatus(OrderStatus.COMPLETED); // test 용 원래 배송준비중임
        orderRepository.save(order); // order 조회 test 목적
    }
}
