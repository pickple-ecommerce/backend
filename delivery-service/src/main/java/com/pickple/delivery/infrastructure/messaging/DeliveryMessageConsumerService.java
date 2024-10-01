package com.pickple.delivery.infrastructure.messaging;

import static com.pickple.common_module.infrastructure.messaging.EventSerializer.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pickple.delivery.application.dto.DeliveryCreateResponseDto;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryMessageConsumerService {

    private final DeliveryApplicationService deliveryApplicationService;

    @KafkaListener(topics = "delivery-service-create")
    public void consumeDeliveryCreation(String message) {
        log.info("메세지 도착: {}", message);
        try {
            DeliveryCreateRequestEvent deliveryCreateRequestEvent = objectMapper.readValue(message,
                    DeliveryCreateRequestEvent.class);
            DeliveryCreateResponseDto delivery = deliveryApplicationService.createDelivery(
                    DeliveryMapper.convertCreateRequestEventToDto(deliveryCreateRequestEvent));
            // TODO: Order 로 이벤트 발행?
        } catch (JsonProcessingException e) {
            // TODO: Exception Handler 구현
            log.error("유효하지 않은 메시지 형식입니다.: {}", message, e);
        }
    }

}