package com.pickple.delivery.infrastructure.messaging;

import static com.pickple.common_module.infrastructure.messaging.EventSerializer.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.exception.DeliveryErrorCode;
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

    // TODO: Kafka errorHandler 구현
    @KafkaListener(topics = "delivery-create-request", groupId = "delivery-group")
    public void consumeDeliveryCreation(String message) {

        DeliveryCreateRequestEvent deliveryCreateRequestEvent;
        try {
            deliveryCreateRequestEvent = objectMapper.readValue(message,
                    DeliveryCreateRequestEvent.class);
        } catch (JsonProcessingException e) {
            // TODO: Exception Handler 구현하여 throw Exception이 아니라 ErrorEvent를 발행해야 합니다.
            log.error("유효하지 않은 메시지 형식입니다.: {}", message, e);
            throw new CustomException(DeliveryErrorCode.INVALID_MESSAGE_FORMAT);
        }

        deliveryApplicationService.createDelivery(
                DeliveryMapper.convertCreateRequestEventToDto(deliveryCreateRequestEvent));
    }

}