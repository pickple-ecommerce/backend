package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.OrderEventService;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateResponseEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryEndResponseEvent;
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
            orderEventService.handlePaymentComplete(orderId, paymentId, event.getMethod());
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
        orderEventService.handleDeliveryCreate(orderId, deliveryId);
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

        // 배송 완료 메시지를 처리
        log.info("배송 완료 메시지를 처리합니다. orderId: {}, deliveryId: {}", orderId, deliveryId);
        orderEventService.handleDeliveryEnd(orderId);
    }
}