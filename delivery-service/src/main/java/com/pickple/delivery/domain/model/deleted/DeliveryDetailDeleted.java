package com.pickple.delivery.domain.model.deleted;

import com.pickple.delivery.domain.model.DeliveryDetail;
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
public class DeliveryDetailDeleted {

    @Field("delivery_detail_status")
    private String deliveryDetailStatus;

    @Field("delivery_detail_description")
    private String deliveryDetailDescription;

    @Field("delivery_detail_time")
    private Date deliveryDetailTime;

    public static DeliveryDetailDeleted fromDeliveryDetail(DeliveryDetail deliveryDetail) {
        return DeliveryDetailDeleted.builder()
                .deliveryDetailStatus(deliveryDetail.getDeliveryDetailStatus())
                .deliveryDetailDescription(deliveryDetail.getDeliveryDetailDescription())
                .deliveryDetailTime(deliveryDetail.getDeliveryDetailTime())
                .build();
    }
}
