package com.pickple.notification_service.application.dto;

import com.pickple.notification_service.domain.model.Notification;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRespDto {
    private UUID notificationId;
    private String channelName;
    private String username;
    private String category;
    private String subject;
    private String content;
    private String sender;

    public static NotificationRespDto from(Notification notification) {
        return NotificationRespDto.builder()
                .notificationId(notification.getNotificationId())
                .channelName(notification.getChannel().getName())
                .username(notification.getUsername())
                .category(notification.getCategory().name())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .sender(notification.getSender())
                .build();
    }
}
