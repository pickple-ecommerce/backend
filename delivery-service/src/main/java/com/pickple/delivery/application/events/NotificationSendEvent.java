package com.pickple.delivery.application.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationSendEvent {
    private String username;

    @Builder.Default
    private String category = "DELIVERY";
    private String subject;

    private String content;

    private String sender;
}
