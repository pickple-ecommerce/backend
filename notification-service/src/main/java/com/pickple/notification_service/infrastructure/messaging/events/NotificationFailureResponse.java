package com.pickple.notification_service.infrastructure.messaging.events;

import com.pickple.common_module.exception.CommonErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationFailureResponse {
    private String username;
    private String errorCode = CommonErrorCode.DATABASE_ERROR.getStatus().toString();
    private LocalDateTime timestamp = LocalDateTime.now();

    public NotificationFailureResponse(String username) {
        this.username = username;
    }
}
