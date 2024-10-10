package com.pickple.notification_service.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessageDto {
    private String to;
    private String from;
    private String message;
}
