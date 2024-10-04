package com.pickple.delivery.domain.model.enums;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.exception.DeliveryErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 배송 유형을 나타내는 ENUM 클래스입니다.
 *
 * <ul>
 *   <li>DIRECT - 직접 배송</li>
 *   <li>COURIER - 택배 배송</li>
 *   <li>EXPRESS - 익일 특급 배송</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum DeliveryType {

    DIRECT("직접 배송"),
    COURIER("택배 배송"),
    EXPRESS("익일 특급 배송");

    private final String typeName;

    public static DeliveryType getDeliveryType(String name) {
        return Arrays.stream(values())
                .filter(type -> type.getTypeName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_TYPE_NOT_SUPPORT));
    }
}