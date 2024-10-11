package com.pickple.notification_service.application.service;

import com.pickple.notification_service.infrastructure.messaging.events.EmailCreateRequestEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    public void sendEmailNotification(@Valid EmailCreateRequestEvent event){
        emailService.sendEmail(event);
    }
}
