package com.pickple.delivery.domain.model.enums;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.exception.DeliveryErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code companyId} 는 배송 추적 API에서 제공되는 carrierId로, 예를 들어 "kr.logen"은 로젠택배를 의미합니다.
 * 자세한 사항은 <a href="https://tracker.delivery/docs/tracking-api#carriers-search">Carrier API 문서</a>를 참고하세요.
 */
@Getter
@AllArgsConstructor
public enum DeliveryCarrier {

    LOZEN("로젠택배",  "kr.logen"),
    CJ_LOGISTICS("CJ대한통운", "kr.cjlogistics"),
    HAJIN("한진택배", "kr.hanjin"),
    EPOST("우체국택배", "kr.epost");

    private final String companyName;

    private final String companyId;

    public static DeliveryCarrier getIdFromCarrierName(String name) {
        return Arrays.stream(values())
                .filter(company -> company.companyName.equals(name))
                .findFirst()
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.CARRIER_NAME_NOT_SUPPORT));
    }

}
