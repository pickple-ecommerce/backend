package com.pickple.notification_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCreateRequestEvent {
    private String username;
    private String category;
    private String toEmail;
    private String subject;
    private String content;

    @Builder.Default
    private String sender = "System";

}
