package com.pickple.user.domain.model;

public enum UserRole {

    USER("USER"),   // 일반 유저
    VENDOR_MANAGER("VENDOR_MANAGER"),   // 업체 관리자
    MASTER("MASTER"); // 마스터 관리자

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getAuthority().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}
