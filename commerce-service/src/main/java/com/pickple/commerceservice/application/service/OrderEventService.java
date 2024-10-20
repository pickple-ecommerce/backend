package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCancelRequestEvent;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderRepository orderRepository;
    private final OrderMessagingProducerService messagingProducerService;
    private final StockService stockService;  // 재고 서비스 추가
    private final KafkaTemplate<String, Object> kafkaTemplate;  // Kafka 메시지 전송용

    /**
     * 결제 완료 처리 후 배송 요청을 보내는 메서드
     */
//    @Transactional
//    public void handlePaymentComplete(UUID orderId, UUID paymentId, String username) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
//                CommerceErrorCode.ORDER_NOT_FOUND));
//        order.assignPaymentId(paymentId);
//
//        String actualUsername = order.getUsername(); // 실제 사용자 이름
//
//        orderRepository.save(order); // order 조회 test 목적
//
//        // 결제 완료 메시지가 오면 호출되는 메서드로, 배송 생성 메시지를 보냄
//        messagingProducerService.sendDeliveryCreateRequest(orderId, actualUsername); // 실제 사용자 이름을 전달
//    }

    @Transactional
    public void handlePaymentComplete(UUID orderId, UUID paymentId, String username) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
                CommerceErrorCode.ORDER_NOT_FOUND));
        order.assignPaymentId(paymentId);

        List<OrderDetail> orderDetails = order.getOrderDetails();

        // 재고 확인 및 차감
        boolean isStockSufficient = true;
        for (OrderDetail detail : orderDetails) {
            StockByProductDto stockDto = stockService.getStockByProductId(detail.getProduct().getProductId());
            if (stockDto.getStockQuantity() < detail.getOrderQuantity()) {
                isStockSufficient = false;
                break;
            }
        }

        // 재고 부족 시 결제 취소 요청
        if (!isStockSufficient) {
            PaymentCancelRequestEvent cancelEvent = new PaymentCancelRequestEvent(orderId, "재고 부족으로 취소 요청");
            kafkaTemplate.send("payment-cancel-request", cancelEvent);
            log.error("재고 부족으로 결제 취소 요청을 전송했습니다: orderId {}", orderId);

            // 주문 상태를 CANCELED로 변경
            order.changeStatus(OrderStatus.CANCELED);
            orderRepository.save(order);

            return;
        }

        // 재고 충분할 경우 재고 차감 및 주문 상태 저장
        for (OrderDetail detail : orderDetails) {
            stockService.decreaseStockQuantityForOrder(detail);
        }

        orderRepository.save(order);

        // 결제 완료 후 배송 생성 요청
        messagingProducerService.sendDeliveryCreateRequest(orderId, order.getUsername());
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
