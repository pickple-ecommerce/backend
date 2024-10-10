package com.pickple.notification_service.infrastructure.messaging;

import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.notification_service.application.service.EmailService;
import com.pickple.notification_service.infrastructure.messaging.events.EmailCreateRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics="email-create-request", groupId="notification-group")
    public void handleCreateRequest(String message){
        EmailCreateRequestEvent event = EventSerializer.deserialize(message, EmailCreateRequestEvent.class);
        emailService.sendEmail(event);
    }
}
