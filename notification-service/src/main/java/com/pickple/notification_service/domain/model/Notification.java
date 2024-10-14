package com.pickple.notification_service.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import com.pickple.notification_service.infrastructure.messaging.events.EmailCreateRequestEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_notifications")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="notification_id")
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name="channel_id", referencedColumnName = "id")
    private Channel channel;

    @Column(name="username")
    private String username;

    @Column(name="category")
    @Enumerated(value=EnumType.STRING)
    private NotificationCategoryEnum category;

    @Column(name="subject")
    private String subject;

    @Column(name="content")
    private String content;

    @Column(name="sender")
    private String sender;

    @Column(name="status")
    private String status;

    public Notification(EmailCreateRequestEvent event, Channel channel){
        this.username = event.getUsername();
        this.channel = channel;
        this.category = NotificationCategoryEnum.valueOf(event.getCategory());
        this.subject = event.getSubject();
        this.content = event.getContent();
        this.sender = event.getSender();
        this.status = "PENDING";
    }

    public void sent () {
        this.status = "SENT";
    }

    public void delete(String deleteBy) {
        this.isDelete = true;
        this.deletedBy = deleteBy;
        this.deletedAt = LocalDateTime.now();
    }

}
