package com.pickple.notification_service.domain.repository;

import com.pickple.notification_service.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByNotificationIdAndIsDeleteIsFalse(UUID notificationId);
    Optional<Page<Notification>> findByUsernameAndIsDeleteIsFalse(String username, Pageable pageable);
    Optional<Page<Notification>> findAllByIsDeleteIsFalse(Pageable pageable);
}
