package com.pickple.notification_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSuccessResponse {
    private UUID notificationId;
    private String status = "SENT";

    public NotificationSuccessResponse(UUID notificationId) {
        this.notificationId = notificationId;
    }
}
