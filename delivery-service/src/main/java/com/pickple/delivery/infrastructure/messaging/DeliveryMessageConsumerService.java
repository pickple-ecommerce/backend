package com.pickple.delivery.infrastructure.messaging;

import static com.pickple.common_module.infrastructure.messaging.EventSerializer.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.exception.ErrorCode;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.exception.DeliveryErrorCode;
import com.pickple.delivery.exception.DeliveryMessageFailureHandler;
import com.pickple.delivery.infrastructure.config.SecurityConfig;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryDeleteRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryMessageConsumerService {

    private final DeliveryApplicationService deliveryApplicationService;

    private final DeliveryMessageFailureHandler deliveryMessageFailureHandler;

    // TODO: Kafka errorHandler 구현
    @KafkaListener(topics = "delivery-create-request", groupId = "delivery-group")
    public void consumeDeliveryCreation(String message) {
        DeliveryCreateRequestEvent event;
        try {
            event = objectMapper.readValue(message,
                    DeliveryCreateRequestEvent.class);
        } catch (JsonProcessingException e) {
            log.error("유효하지 않은 메시지 형식입니다.: {}", message, e);
            throw new CustomException(DeliveryErrorCode.INVALID_MESSAGE_FORMAT);
        }

        SecurityConfig.setSecurityContext(event.getUsername());

        try {
            deliveryApplicationService.createDelivery(
                    DeliveryMapper.convertCreateRequestEventToDto(event));
        } catch (Exception e) {
            deliveryMessageFailureHandler.handleDeliveryCreateFailure(event,
                    DeliveryErrorCode.DELIVERY_CREATE_FAILURE.getMessage());
        }
    }

    @KafkaListener(topics = "delivery-delete-request", groupId = "delivery-group")
    public void consumeDeliveryDeletion(String message) {
        DeliveryDeleteRequestEvent event;
        try {
            event = objectMapper.readValue(message, DeliveryDeleteRequestEvent.class);
        } catch (JsonProcessingException e) {
            log.error("유효하지 않은 메시지 형식입니다.: {}", message, e);
            throw new CustomException(DeliveryErrorCode.INVALID_MESSAGE_FORMAT);
        }

        try {
            deliveryApplicationService.deleteDelivery(event.getDeliveryId(), event.getDeleter());
        } catch (Exception e) {
            deliveryMessageFailureHandler.handleDeliveryDeleteFailure(event,
                    DeliveryErrorCode.DELIVERY_DELETE_FAILURE.getMessage());
            throw new CustomException(DeliveryErrorCode.DELIVERY_DELETE_FAILURE);
        }
    }

}