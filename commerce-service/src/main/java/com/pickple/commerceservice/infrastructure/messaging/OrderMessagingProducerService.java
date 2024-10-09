package com.pickple.commerceservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.application.service.TemporaryStorageService;
import com.pickple.commerceservice.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMessagingProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TemporaryStorageService temporaryStorageService;
    private final ObjectMapper objectMapper; // ObjectMapper 주입

    public void sendPaymentRequest(UUID orderId, Object paymentInfo) {
        // Kafka 메시지 발행
        String topic = "payment-request";
        String message = "결제 요청 메시지: Order ID: " + orderId + ", Payment Info: " + paymentInfo.toString();
        kafkaTemplate.send(topic, message);
    }

    public void sendDeliveryCreateRequest(UUID orderId) {
        // 저장된 배송 정보를 가져온 후 배송 생성 요청 메시지를 보냄
        Object deliveryInfoObj = temporaryStorageService.getDeliveryInfo(orderId);

        if (deliveryInfoObj instanceof OrderCreateRequestDto.DeliveryInfo deliveryInfo) {
            // 배송 정보가 정상적으로 조회된 경우
            DeliveryCreateRequestEvent event = new DeliveryCreateRequestEvent(
                    orderId, // 주문 ID
                    deliveryInfo.getDeliveryRequirement(), // 배송 요청 사항
                    deliveryInfo.getRecipientName(), // 수령인 이름
                    deliveryInfo.getAddress(), // 배송 주소
                    deliveryInfo.getContact() // 수령인 연락처
            );

            try {
                // event 객체를 JSON 문자열로 변환
                String eventJson = objectMapper.writeValueAsString(event);
                kafkaTemplate.send("delivery-create-request", eventJson);
                temporaryStorageService.removeDeliveryInfo(orderId); // 성공적으로 메시지 전송 후 삭제
            } catch (JsonProcessingException e) {
                log.error("배송 생성 요청 메시지를 직렬화하는 도중 오류가 발생했습니다.", e);
            }
        } else {
            log.error("주문 ID {}에 대한 배송 정보를 찾을 수 없습니다.", orderId);
        }
    }

}
