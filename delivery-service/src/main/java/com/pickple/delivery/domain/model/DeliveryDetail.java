package com.pickple.delivery.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("p_delivery_details")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDetail extends BaseEntity {

    @PrimaryKey
    @Column("delivery_detail_id")
    private DeliveryDetailId deliveryDetailId;

    @Column("delivery_detail_status")
    private String deliveryDetailStatus;

    @Column("delivery_detail_description")
    private String deliveryDetailDescription;

}