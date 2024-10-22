package com.pickple.commerceservice.infrastructure.messaging;

import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCancelRequestEvent;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderRepository orderRepository;
    private final OrderMessagingProducerService messagingProducerService;
    private final StockService stockService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * payment-create-response
     */
    @Transactional
    public void handlePaymentComplete(UUID orderId, UUID paymentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 재고 차감
        decreaseStockForOrder(order);

        // 결제 ID 지정
        order.assignPaymentId(paymentId);

        // 주문 상태 저장
        orderRepository.save(order);

        // 배송 생성 요청
        messagingProducerService.sendDeliveryCreateRequest(orderId, order.getUsername());
    }

    /**
     * delivery-create-response
     */
    @Transactional
    public void handleDeliveryComplete(UUID orderId, UUID deliveryId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 주문 ID 지정
        order.assignDeliveryId(deliveryId);

        orderRepository.save(order);
    }

    /**
     * delivery-end-response(complete)
     */
    @Transactional
    public void handleDeliveryEnd(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        order.changeStatus(OrderStatus.COMPLETED);
    }

    /**
     * payment-cancel-response
     */
    @Transactional
    public void handlePaymentCancel(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        increaseStockForOrder(order); // 재고 복구
        order.changeStatus(OrderStatus.CANCELED);  // 주문 취소 처리
        order.assignPaymentId(null);  // 결제 ID 연관성 제거
        orderRepository.save(order);  // 변경된 주문 저장
    }

    /**
     * delivery-end-response(cancel)
     */
    @Transactional
    public void handleDeliveryCancel(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        sendPaymentCancelRequest(orderId);
        order.assignDeliveryId(null); // 배송 ID 연관성 제거
        orderRepository.save(order);  // 변경된 주문 저장
    }

    // 재고 차감
    private void decreaseStockForOrder(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail detail : orderDetails) {
            stockService.decreaseStockQuantityForOrder(detail);
        }
    }

    // 재고 복구
    private void increaseStockForOrder(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail detail : orderDetails) {
            stockService.increaseStockQuantityForOrder(detail);
        }
    }

    // 배송 취소로 인한 결제 취소 요청
    private void sendPaymentCancelRequest(UUID orderId) {
        PaymentCancelRequestEvent cancelEvent = new PaymentCancelRequestEvent(orderId, "배송 취소로 인한 결제 취소 요청");
        kafkaTemplate.send("payment-cancel-request", cancelEvent);
    }
}