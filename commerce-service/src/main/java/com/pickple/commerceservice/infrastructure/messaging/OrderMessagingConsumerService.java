package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateResponseEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryEndResponseEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCancelResponseEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCreateResponseEvent;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMessagingConsumerService {

    private final ObjectMapper objectMapper;
    private final OrderEventService orderEventService;

    @KafkaListener(topics = "payment-create-response", groupId = "commerce-service")
    public void listenPaymentCreateResponse(ConsumerRecord<String, String> record) {
        PaymentCreateResponseEvent event;
        try {
            event = objectMapper.readValue(record.value(), PaymentCreateResponseEvent.class);
        } catch (JsonProcessingException e) {
            log.error("결제 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
            return;  // 예외 발생 시 메시지 처리를 종료
        }

        if (!"COMPLETED".equalsIgnoreCase(event.getStatus())) {
            log.warn("결제 실패 또는 다른 상태: {}", event.getStatus());
            return;  // 결제 실패 시에도 메시지 처리를 중단
        }

        UUID orderId = event.getOrderId();
        UUID paymentId = event.getPaymentId();

        try {
            orderEventService.handlePaymentComplete(orderId, paymentId);
        } catch (CustomException e) {
            // 재고 부족 시 예외를 던지지 않고 로그만 남기고 처리를 종료
            log.error("결제 처리 중 재고 부족으로 결제가 취소되었습니다. orderId: {}, paymentId: {}", orderId, paymentId);
        }
    }

    @KafkaListener(topics = "delivery-create-response", groupId = "commerce-service")
    public void listenDeliveryCreateResponse(String message) {
        DeliveryCreateResponseEvent event;
        try {
            event = objectMapper.readValue(message, DeliveryCreateResponseEvent.class);
        } catch (JsonProcessingException e) {
            log.error("배송 생성 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
            throw new CustomException(CommerceErrorCode.INVALID_DELIVERY_MESSAGE_FORMAT);
        }

        UUID orderId = event.getOrderId();
        UUID deliveryId = event.getDeliveryId();
        log.info("배송 생성 메시지를 처리합니다. orderId: {}, deliveryId: {}", orderId, deliveryId);
        orderEventService.handleDeliveryComplete(orderId, deliveryId);
    }

@KafkaListener(topics = "delivery-end-response", groupId = "commerce-service")
public void listenDeliveryEndResponse(String message) {
    DeliveryEndResponseEvent event;
    try {
        // 메시지를 DeliveryEndResponseEvent로 역직렬화
        event = objectMapper.readValue(message, DeliveryEndResponseEvent.class);
    } catch (JsonProcessingException e) {
        log.error("배송 완료 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
        throw new CustomException(CommerceErrorCode.INVALID_DELIVERY_MESSAGE_FORMAT);
    }

    UUID orderId = event.getOrderId();
    UUID deliveryId = event.getDeliveryId();
    String status = event.getStatus();  // status 값 가져오기

    // 배송 완료 메시지를 처리
    log.info("배송 완료 메시지를 처리합니다. orderId: {}, deliveryId: {}, status: {}", orderId, deliveryId, status);

    // status에 따라 처리
    if ("COMPLETED".equalsIgnoreCase(status)) {
        orderEventService.handleOrderComplete(orderId);  // 배송 완료 처리
    } else if ("DELETED".equalsIgnoreCase(status)) {
        orderEventService.handleOrderCancel(orderId);  // 주문 취소 처리
    } else {
        log.warn("알 수 없는 상태: {}", status);
    }

    }

    @KafkaListener(topics = "payment-cancel-response", groupId = "commerce-service")
    public void listenPaymentCancelResponse(String message) {
        PaymentCancelResponseEvent event;
        try {
            // 메시지를 PaymentCancelResponseEvent로 역직렬화
            event = objectMapper.readValue(message, PaymentCancelResponseEvent.class);
        } catch (JsonProcessingException e) {
            log.error("결제 취소 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
            throw new CustomException(CommerceErrorCode.INVALID_PAYMENT_MESSAGE_FORMAT);
        }

        UUID orderId = event.getOrderId();
        String status = event.getStatus();  // status 값 가져오기

        // 결제 취소 상태에 따라 처리
        if ("CANCELED".equalsIgnoreCase(status)) {
            orderEventService.handlePaymentCancel(orderId);  // 결제 취소 처리
        } else {
            log.warn("결제 취소 실패 또는 알 수 없는 상태: {}", status);
        }
    }

}