package com.pickple.commerceservice.infrastructure.messaging;

import com.pickple.commerceservice.infrastructure.messaging.events.*;
import com.pickple.commerceservice.infrastructure.redis.TemporaryStorageService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderMessagingProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TemporaryStorageService temporaryStorageService;

    @Value("${kafka.topic.delivery-create-request}")
    private String deliveryCreateRequestTopic;

    @Value("${kafka.topic.payment-create-request}")
    private String paymentCreateRequestTopic;

    @Value("${kafka.topic.payment-cancel-request}")
    private String paymentCancelRequestTopic;

    @Value("${kafka.topic.delivery-delete-request}")
    private String deliveryDeleteRequestTopic;

    @Value("${kafka.topic.notification-create-request}")
    private String notificationCreateRequestTopic;

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
        // Redis에 저장된 배송 정보 조회
        OrderCreateRequestDto.DeliveryInfo deliveryInfo = temporaryStorageService.getDeliveryInfo(orderId);

        // 배송 생성 이벤트 객체 생성
        DeliveryCreateRequestEvent event = new DeliveryCreateRequestEvent(
                orderId,
                deliveryInfo.getDeliveryRequirement(),
                deliveryInfo.getRecipientName(),
                deliveryInfo.getAddress(),
                deliveryInfo.getContact(),
                username
        );

        // delivery-create-request 메시지 전송
        sendMessage(deliveryCreateRequestTopic, event);
        // Redis에 저장된 배송 정보 삭제
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
    public void sendDeliveryDeleteRequest(UUID deliveryId, UUID orderId, String username) {
        DeliveryDeleteRequestEvent event = new DeliveryDeleteRequestEvent(deliveryId, orderId, username);
        sendMessage(deliveryDeleteRequestTopic, event);
    }

    /**
     * 알림 생성 요청 전송
     */
    public void sendNotificationCreateRequest(String username, String role, String sender, String subject, String content, String category) {
        // sender가 null일 경우 기본값으로 "System" 설정
        if (sender == null || sender.isEmpty()) {
            sender = "System";
        }

        NotificationCreateRequestEvent event = new NotificationCreateRequestEvent(
                username,
                role,
                sender,
                subject,
                content,
                category
        );

        sendMessage(notificationCreateRequestTopic, event);
    }

    /**
     * 공통 메시지 전송 메소드
     */
    private void sendMessage(String topic, Object event) {
        kafkaTemplate.send(topic, event);
    }
}