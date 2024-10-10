package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.infrastructure.messaging.events.PaymentCreateResponseEvent;
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

    @KafkaListener(topics = "payment-create-response", groupId = "commerce-service")
    public void listenPaymentCreateResponse(ConsumerRecord<String, String> record) {
        try {
            // JSON 문자열을 PaymentCreateResponseEvent 객체로 변환
            PaymentCreateResponseEvent event = objectMapper.readValue(record.value(), PaymentCreateResponseEvent.class);

            // 결제 상태가 "COMPLETED"일 경우 처리
            if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {
                UUID orderId = event.getOrderId();
                String username = event.getMethod(); // 필요에 따라 method 대신 다른 필드 사용 가능

                // OrderService를 통해 결제 완료 처리 및 배송 요청 전송
                orderService.handlePaymentComplete(orderId, username);
            } else {
                log.warn("결제 실패 또는 다른 상태: {}", event.getStatus());
            }
        } catch (JsonProcessingException e) {
            log.error("결제 응답 메시지를 역직렬화하는 도중 오류가 발생했습니다.", e);
        }
    }
}