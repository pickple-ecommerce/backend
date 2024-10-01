package com.pickple.delivery.domain.model.enums;

/**
 * 배송 상태를 나타내는 ENUM 클래스입니다.
 *
 * <ul>
 *   <li>PENDING - 배송 준비 중으로, 배송이 아직 시작되지 않은 상태.</li>
 *   <li>IN_TRANSIT - 배송중. 이 상태에서는 배송을 취소할 수 없습니다.</li>
 *   <li>DELIVERED - 배송 완료</li>
 * </ul>
 */
public enum DeliveryStatus {
    PENDING,
    IN_TRANSIT,
    DELIVERED
}