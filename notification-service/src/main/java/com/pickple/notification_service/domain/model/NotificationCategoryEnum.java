package com.pickple.notification_service.domain.model;

public enum NotificationCategoryEnum {
    USER("회원"),
    PRODUCT("상품"),
    VENDOR("업체"),
    ORDER("주문"),
    PAYMENT("결제"),
    DELIVERY("배송");

    private final String category;

    NotificationCategoryEnum(String category) {
        this.category = category;
    }
}
