package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.TemporaryStorageService;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryDeleteRequestEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCancelRequestEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCreateRequestEvent;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMessagingProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TemporaryStorageService temporaryStorageService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.delivery-create-request}")
    private String deliveryCreateRequestTopic;

    @Value("${kafka.topic.payment-create-request}")
    private String paymentCreateRequestTopic;

    @Value("${kafka.topic.payment-cancel-request}")
    private String paymentCancelRequestTopic;

    @Value("${kafka.topic.delivery-delete-request}")
    private String deliveryDeleteRequestTopic;

    /**
     * 결제 요청 전송
     */
    public void sendPaymentRequest(UUID orderId, BigDecimal amount, String username) {
        PaymentCreateRequestEvent event = new PaymentCreateRequestEvent(orderId, amount, username);
        sendMessage(paymentCreateRequestTopic, event);
    }

    /**
     * 배송 생성 요청 전송
     */
    public void sendDeliveryCreateRequest(UUID orderId, String username) {
        // 저장된 배송 정보를 조회
        OrderCreateRequestDto.DeliveryInfo deliveryInfo =
                temporaryStorageService.getDeliveryInfo(orderId);

        // 배송 생성 이벤트 객체 생성
        DeliveryCreateRequestEvent event = new DeliveryCreateRequestEvent(
                orderId,
                deliveryInfo.getDeliveryRequirement(),
                deliveryInfo.getRecipientName(),
                deliveryInfo.getAddress(),
                deliveryInfo.getContact(),
                username
        );

        // Kafka 메시지 전송 및 성공 시 저장된 배송 정보 삭제
        sendMessage(deliveryCreateRequestTopic, event);
        temporaryStorageService.removeDeliveryInfo(orderId);
    }

    /**
     * 결제 취소 요청 전송
     */
    public void sendPaymentCancelRequest(UUID orderId) {
        PaymentCancelRequestEvent event = new PaymentCancelRequestEvent(orderId, "Order cancellation request.");
        sendMessage(paymentCancelRequestTopic, event);
    }

    /**
     * 배송 취소 요청 전송
     */
    public void sendDeliveryDeleteRequest(UUID deliveryId, UUID orderId) {
        DeliveryDeleteRequestEvent event = new DeliveryDeleteRequestEvent(deliveryId, orderId, "Order cancellation request.");
        sendMessage(deliveryDeleteRequestTopic, event);
    }

    /**
     * 공통 메시지 전송 메소드
     */
    private void sendMessage(String topic, Object event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, eventJson);
            log.info("{} 이벤트를 전송했습니다: {}", topic, eventJson);
        } catch (JsonProcessingException e) {
            log.error("{} 이벤트 직렬화 실패: {}", topic, e.getMessage());
        }
    }
}