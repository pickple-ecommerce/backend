package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.OrderEventService;
import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateResponseEvent;
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
    private final OrderService orderService;
    private final OrderEventService orderEventService;

    @KafkaListener(topics = "payment-create-response", groupId = "commerce-service")
    public void listenPaymentCreateResponse(ConsumerRecord<String, String> record) {
        PaymentCreateResponseEvent event;
        try {
            // JSON 문자열을 PaymentCreateResponseEvent 객체로 변환
            event = objectMapper.readValue(record.value(),
                    PaymentCreateResponseEvent.class);
        } catch (JsonProcessingException e) {
            log.error("결제 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
            throw new CustomException(CommerceErrorCode.INVALID_PAYMENT_MESSAGE_FORMAT);
        }

        // 결제 상태가 "COMPLETED"가 아닐 경우 처리
        if (!("COMPLETED".equalsIgnoreCase(event.getStatus()))) {
            log.warn("결제 실패 또는 다른 상태: {}", event.getStatus());
            throw new CustomException(CommerceErrorCode.PAYMENT_CREATE_FAILED);
        }

        UUID orderId = event.getOrderId();
        UUID paymentId = event.getPaymentId();
        String username = event.getMethod(); // 필요에 따라 method 대신 다른 필드 사용 가능

        // OrderService를 통해 결제 완료 처리 및 배송 요청 전송
        log.info("결제 완료 메시지를 처리합니다. orderId: {}, paymentId: {}", orderId, paymentId);
        orderEventService.handlePaymentComplete(orderId, paymentId, username);
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
}