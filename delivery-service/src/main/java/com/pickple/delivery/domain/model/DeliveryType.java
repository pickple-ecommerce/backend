package com.pickple.delivery.domain.model;

/**
 * 배송 유형을 나타내는 ENUM 클래스입니다.
 *
 * <ul>
 *   <li>PICKUP - 직접 수령</li>
 *   <li>COURIER - 택배 배송</li>
 *   <li>EXPRESS - 익일 특급 배송</li>
 * </ul>
 */
public enum DeliveryType {
    PICKUP,
    COURIER,
    EXPRESS,
}