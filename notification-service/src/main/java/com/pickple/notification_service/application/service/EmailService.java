package com.pickple.notification_service.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.notification_service.domain.model.Channel;
import com.pickple.notification_service.domain.model.Notification;
import com.pickple.notification_service.domain.repository.ChannelRepository;
import com.pickple.notification_service.domain.repository.NotificationRepository;
import com.pickple.notification_service.exception.ChannelErrorCode;
import com.pickple.notification_service.exception.NotificationErrorCode;
import com.pickple.notification_service.infrastructure.feign.UserFeignClient;
import com.pickple.notification_service.infrastructure.messaging.events.EmailCreateRequestEvent;
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

    @Autowired
    private final NotificationRepository emailRepository;
    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    private final UserFeignClient userFeignClient;

    // 이메일 전송
    public void sendEmail(EmailCreateRequestEvent event) {

        String toEmail = userFeignClient.getUserEmail(event.getUsername(), event.getUsername(), event.getRole());

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(event.getSubject());
            helper.setText(event.getContent(), true);
            helper.setFrom(event.getSender());
            helper.setReplyTo(from);

            mailSender.send(message);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(NotificationErrorCode.EMAIL_CREATE_ERROR);

        } catch(Exception e){
            log.error(e.getMessage(), e);
            throw new CustomException(NotificationErrorCode.EMAIL_SENDING_ERROR);

        }

        saveEmail(event);
    }

    // 이메일 저장
    public void saveEmail(EmailCreateRequestEvent event) {
        // 알림 채널 조회
        Channel channel = channelRepository.findByNameAndIsDeleteIsFalse("Email").orElseThrow(
                ()-> new CustomException(ChannelErrorCode.CHANNEL_NOT_FOUND)
        );

        Notification email = new Notification(event, channel);

        try {

            emailRepository.save(email);

        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new CustomException(CommonErrorCode.DATABASE_ERROR);

        }

        // 상태 변경
        email.sent();
        emailRepository.save(email);
    }


}
