package com.pickple.notification_service.infrastructure.messaging.events;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String subject;
    @NotBlank(message = "본문은 비워둘 수 없습니다.")
    private String content;

    @Builder.Default
    private String sender = "System";

}
