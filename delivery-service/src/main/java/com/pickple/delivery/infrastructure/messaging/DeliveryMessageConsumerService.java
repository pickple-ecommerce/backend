package com.pickple.delivery.infrastructure.messaging;

import static com.pickple.common_module.infrastructure.messaging.EventSerializer.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.exception.DeliveryErrorCode;
import com.pickple.delivery.exception.DeliveryMessageFailureHandler;
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
            // TODO: JSON parsing이 실패한 orderId는 어떻게 추출할까요?
            throw new CustomException(DeliveryErrorCode.INVALID_MESSAGE_FORMAT);
        }
        try {
            deliveryApplicationService.createDelivery(
                    DeliveryMapper.convertCreateRequestEventToDto(event));
        }
        catch (Exception e) {
            deliveryMessageFailureHandler.handleDeliveryCreateFailure(event);
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
        } catch (CustomException e) {
            log.error("배송 삭제에 실패하였습니다.: {}", message, e);
            deliveryMessageFailureHandler.handleDeliveryDeleteFailure(event);
            throw new CustomException(e.getErrorCode());
        }
    }

}