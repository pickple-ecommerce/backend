package com.pickple.notification_service.application.dto;
import com.pickple.notification_service.domain.model.Channel;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateRespDto {
    private UUID id;
    private String name;
    private String description;

    public static ChannelCreateRespDto from(Channel channel) {
        return ChannelCreateRespDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .build();
    }
}
