package com.pickple.notification_service.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import com.pickple.notification_service.application.dto.ChannelCreateRespDto;
import com.pickple.notification_service.presentation.request.ChannelCreateReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_notification_channels")
public class Channel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "Channel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications;

    public Channel(ChannelCreateReqDto reqDto) {
        this.name = reqDto.getName();
        this.description = reqDto.getDescription();
    }

    public void delete(String deleteBy) {
        this.isDelete = true;
        this.deletedBy = deleteBy;
        this.deletedAt = LocalDateTime.now();
    }
}
