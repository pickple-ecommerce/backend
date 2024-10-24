package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateRequestEvent {
    private String username;
    private String role;
    private String sender;
    private String subject;
    private String content;
    private String category;
}
