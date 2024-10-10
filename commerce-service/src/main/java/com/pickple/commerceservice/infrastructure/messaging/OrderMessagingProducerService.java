package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.TemporaryStorageService;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateRequestEvent;
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

    public void sendPaymentRequest(UUID orderId, BigDecimal amount, String username) {
        // PaymentRequestEvent 생성
        PaymentCreateRequestEvent event = new PaymentCreateRequestEvent(orderId, amount, username);

        try {
            // event 객체를 JSON 문자열로 변환
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(paymentCreateRequestTopic, eventJson);
        } catch (JsonProcessingException e) {
            log.error("결제 요청 메시지를 직렬화하는 도중 오류가 발생했습니다.", e);
        }
    }

    public void sendDeliveryCreateRequest(UUID orderId, String username) {
        // 저장된 배송 정보를 가져온 후 배송 생성 요청 메시지를 보냄
        Object deliveryInfoObj = temporaryStorageService.getDeliveryInfo(orderId);

        if (deliveryInfoObj instanceof OrderCreateRequestDto.DeliveryInfo deliveryInfo) {
            // 배송 정보가 정상적으로 조회된 경우
            DeliveryCreateRequestEvent event = new DeliveryCreateRequestEvent(
                    orderId,
                    deliveryInfo.getDeliveryRequirement(),
                    deliveryInfo.getRecipientName(),
                    deliveryInfo.getAddress(),
                    deliveryInfo.getContact(),
                    username
            );

            try {
                // event 객체를 JSON 문자열로 변환
                String eventJson = objectMapper.writeValueAsString(event);
                kafkaTemplate.send(deliveryCreateRequestTopic, eventJson);
                temporaryStorageService.removeDeliveryInfo(orderId); // 성공적으로 메시지 전송 후 삭제
            } catch (JsonProcessingException e) {
                log.error("배송 생성 요청 메시지를 직렬화하는 도중 오류가 발생했습니다.", e);
            }
        } else {
            log.error("주문 ID {}에 대한 배송 정보를 찾을 수 없습니다.", orderId);
        }
    }
}