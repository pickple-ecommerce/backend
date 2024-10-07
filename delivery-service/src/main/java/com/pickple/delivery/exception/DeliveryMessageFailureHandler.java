package com.pickple.delivery.exception;

import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.delivery.application.events.DeliveryCreateFailureEvent;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryDeleteFailureEvent;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryDeleteRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryMessageFailureHandler {

    @Value("${kafka.topic.delivery-create-failure}")
    private String deliveryCreateFailureTopic;

    @Value("${kafka.topic.delivery-delete-failure}")
    private String deliveryDeleteFailureTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handleDeliveryCreateFailure(DeliveryCreateRequestEvent dto, String failureMessage) {
        log.info("Kafka 실패 메시지를 발행합니다. Topic: {}, 주문 ID: {}", deliveryCreateFailureTopic,
                dto.getOrderId());
        DeliveryCreateFailureEvent failureEvent = new DeliveryCreateFailureEvent(
                dto.getOrderId(), failureMessage);
                kafkaTemplate.send(deliveryCreateFailureTopic,
                EventSerializer.serialize(failureEvent));
    }
    public void handleDeliveryDeleteFailure(DeliveryDeleteRequestEvent dto, String failureMessage) {
        log.info("Kafka 실패 메시지를 발행합니다. Topic: {}, 주문 ID: {}", deliveryDeleteFailureTopic,
                dto.getOrderId());
        DeliveryDeleteFailureEvent failureEvent = new DeliveryDeleteFailureEvent(dto.getDeliveryId(),
                dto.getOrderId(), failureMessage);
        kafkaTemplate.send(deliveryCreateFailureTopic,
                EventSerializer.serialize(failureEvent));
    }

}
