package com.pickple.notification_service.application.service;

import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.notification_service.infrastructure.messaging.events.NotificationFailureResponse;
import com.pickple.notification_service.infrastructure.messaging.events.NotificationSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendSuccessEvent(NotificationSuccessResponse event){
        kafkaTemplate.send("notification-success-response", EventSerializer.serialize(event));
    }

    public void sendFailureEvent(NotificationFailureResponse event){
        kafkaTemplate.send("notification-failure-response", EventSerializer.serialize(event));
    }
}
