package com.pickple.notification_service.domain.model;

public enum NotificationStatusEnum {

    PENDING("대기"),
    SENT("전송 완료"),
    FAILED("전송 실패");

    private final String status;

    NotificationStatusEnum(String status) {
        this.status = status;
    }
}
