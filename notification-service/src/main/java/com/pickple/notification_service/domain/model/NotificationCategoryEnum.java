package com.pickple.notification_service.domain.model;

public enum NotificationCategoryEnum {
    USER("USER"),
    PRODUCT("PRODUCT"),
    VENDOR("VENDOR"),
    ORDER("ORDER"),
    PAYMENT("PAYMENT"),
    DELIVERY("DELIVERY");

    private final String category;

    NotificationCategoryEnum(String category) {
        this.category = category;
    }
}
