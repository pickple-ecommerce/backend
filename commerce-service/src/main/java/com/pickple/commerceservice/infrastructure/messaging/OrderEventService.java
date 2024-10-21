package com.pickple.commerceservice.infrastructure.messaging;

import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCancelRequestEvent;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderRepository orderRepository;
    private final OrderMessagingProducerService messagingProducerService;
    private final StockService stockService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void handlePaymentComplete(UUID orderId, UUID paymentId) {
        Order order = findOrderById(orderId);
        order.assignPaymentId(paymentId);

        if (!isStockSufficient(order)) {
            sendPaymentCancelRequest(orderId, "재고 부족으로 결제 취소 요청");
            cancelOrder(order);
            return;
        }

        decreaseStockForOrder(order);
        orderRepository.save(order);
        messagingProducerService.sendDeliveryCreateRequest(orderId, order.getUsername());
    }

    @Transactional
    public void handleDeliveryComplete(UUID orderId, UUID deliveryId) {
        Order order = findOrderById(orderId);
        order.assignDeliveryId(deliveryId);
        orderRepository.save(order);
        log.info("배송 완료 처리됨: orderId {}, deliveryId {}", orderId, deliveryId);
    }

    @Transactional
    public void handleOrderComplete(UUID orderId) {
        changeOrderStatus(orderId, OrderStatus.COMPLETED);
        log.info("주문 완료 처리됨: orderId {}", orderId);
    }

    @Transactional
    public void handleOrderCancel(UUID orderId) {
        Order order = findOrderById(orderId);
        sendPaymentCancelRequest(orderId, "배송 취소로 인한 결제 취소 요청");
        restoreStockForOrder(order);
        cancelOrder(order);
    }

    // 내부 메서드

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));
    }

    private boolean isStockSufficient(Order order) {
        for (OrderDetail detail : order.getOrderDetails()) {
            StockByProductDto stockDto = stockService.getStockByProductId(detail.getProduct().getProductId());
            if (stockDto.getStockQuantity() < detail.getOrderQuantity()) {
                return false;
            }
        }
        return true;
    }

    private void decreaseStockForOrder(Order order) {
        for (OrderDetail detail : order.getOrderDetails()) {
            stockService.decreaseStockQuantityForOrder(detail);
        }
        log.info("재고 차감 완료: orderId {}", order.getOrderId());
    }

    private void restoreStockForOrder(Order order) {
        for (OrderDetail detail : order.getOrderDetails()) {
            stockService.increaseStockQuantityForOrder(detail);
        }
        log.info("재고 복구 완료: orderId {}", order.getOrderId());
    }

    private void cancelOrder(Order order) {
        order.changeStatus(OrderStatus.CANCELED);
        order.assignDeliveryId(null);  // 배송 ID를 null로 설정하여 배송과의 연관성 제거
        orderRepository.save(order);
        log.info("주문 취소 처리됨: orderId {}", order.getOrderId());
    }

    /**
     * 주문 상태를 변경하는 메서드
     */
    private void changeOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        order.changeStatus(newStatus);
        orderRepository.save(order);
        log.info("주문 상태 변경됨: orderId {}, newStatus {}", orderId, newStatus);
    }

    private void sendPaymentCancelRequest(UUID orderId, String reason) {
        PaymentCancelRequestEvent cancelEvent = new PaymentCancelRequestEvent(orderId, reason);
        kafkaTemplate.send("payment-cancel-request", cancelEvent);
        log.info("결제 취소 요청 전송: orderId {}, reason: {}", orderId, reason);
    }

    @Transactional
    public void handlePaymentCancel(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 결제 ID를 null로 설정하여 결제와의 연관성 제거
        order.assignPaymentId(null);

        // 주문 취소 처리 (필요 시)
        order.changeStatus(OrderStatus.CANCELED);

        // 재고 복구 처리
        restoreStockForOrder(order);

        // 변경된 주문 저장
        orderRepository.save(order);

        log.info("결제가 취소되었고 재고가 복구되었습니다. orderId: {}", orderId);
    }
}