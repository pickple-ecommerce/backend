package com.pickple.notification_service.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.notification_service.domain.model.Notification;
import com.pickple.notification_service.domain.repository.NotificationRepository;
import com.pickple.notification_service.exception.NotificationErrorCode;
import com.pickple.notification_service.infrastructure.messaging.events.EmailCreateRequestEvent;
import com.pickple.notification_service.infrastructure.messaging.events.NotificationFailureResponse;
import com.pickple.notification_service.infrastructure.messaging.events.NotificationSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    private final NotificationEventProducer notificationEventProducer;

    @Autowired
    private final NotificationRepository emailRepository;

    // 이메일 전송
    public void sendEmail(EmailCreateRequestEvent event) {

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(event.getToEmail());
            helper.setSubject(event.getSubject());
            helper.setText(event.getContent(), true);
            helper.setFrom(event.getSender());
            helper.setReplyTo(from);

            try {
                mailSender.send(message);
                saveEmail(event);
            }catch(RuntimeException e){
                log.error(e.getMessage(), e);
                NotificationFailureResponse failureResponse = new NotificationFailureResponse(event.getUsername());
                notificationEventProducer.sendFailureEvent(failureResponse);
                throw new CustomException(NotificationErrorCode.EMAIL_SENDING_ERROR);
            }
        } catch (MessagingException e) {
            NotificationFailureResponse failureResponse = new NotificationFailureResponse(event.getUsername());
            notificationEventProducer.sendFailureEvent(failureResponse);
            log.error(e.getMessage(), e);
            throw new CustomException(NotificationErrorCode.EMAIL_SENDING_ERROR);
        }
    }

    // 이메일 저장
    public void saveEmail(EmailCreateRequestEvent event) {
        Notification email = new Notification(event);

        try {
            emailRepository.save(email);
        }catch (Exception e){
            NotificationFailureResponse failure = new NotificationFailureResponse(event.getUsername());
            notificationEventProducer.sendFailureEvent(failure);
            throw new CustomException(CommonErrorCode.DATABASE_ERROR);
        }

        email.sent();
        emailRepository.save(email);

        NotificationSuccessResponse success = new NotificationSuccessResponse(email.getNotificationId());

    }


}
