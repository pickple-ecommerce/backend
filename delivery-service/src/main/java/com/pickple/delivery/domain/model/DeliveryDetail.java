package com.pickple.delivery.domain.model;

import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDetail {

    @Field("delivery_detail_status")
    private String deliveryDetailStatus;

    @Field("delivery_detail_description")
    private String deliveryDetailDescription;

    @Field("delivery_detail_time")
    private Date deliveryDetailTime;

    public static DeliveryDetail createFrom(DeliveryDetailCreateRequestDto dto) {
        return DeliveryDetail.builder()
                .deliveryDetailTime(dto.getDeliveryDetailTime())
                .deliveryDetailDescription(dto.getDeliveryDetailDescription())
                .deliveryDetailStatus(dto.getDeliveryDetailStatus())
                .build();
    }
}

