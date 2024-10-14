package com.pickple.notification_service.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateReqDto {
    @NotBlank
    private String name;
    private String description;
}
