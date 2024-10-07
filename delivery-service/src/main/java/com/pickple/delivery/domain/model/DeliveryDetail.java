package com.pickple.delivery.domain.model;

import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Persistable;

@Table("p_delivery_details")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDetail extends BaseEntity implements Persistable<DeliveryDetailId> {

    @PrimaryKey
    @Column("delivery_detail_id")
    private DeliveryDetailId deliveryDetailId;

    @Column("delivery_detail_status")
    private String deliveryDetailStatus;

    @Column("delivery_detail_description")
    private String deliveryDetailDescription;

    public static DeliveryDetail createFrom(DeliveryDetailCreateRequestDto dto) {
        return DeliveryDetail.builder()
                .deliveryDetailId(DeliveryDetailId.builder()
                        .deliveryId(dto.getDeliveryId())
                        .deliveryDetailTime(dto.getDeliveryDetailTime())
                        .build())
                .deliveryDetailDescription(dto.getDeliveryDetailDescription())
                .deliveryDetailStatus(dto.getDeliveryDetailStatus())
                .build();
    }

    @Override
    public DeliveryDetailId getId() {
        return deliveryDetailId;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

}