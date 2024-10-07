package com.pickple.delivery.domain.model;

import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Persistable;

@Table("p_deliveries")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity implements Persistable<UUID> {

    @PrimaryKey(value = "delivery_id")
    @CassandraType(type = Name.UUID)
    @Builder.Default
    private UUID deliveryId = UUID.randomUUID();

    @Column(value = "order_id")
    @CassandraType(type = Name.UUID)
    private UUID orderId;

    @Column(value = "carrier_id")
    private String carrierId;

    @Column(value = "carrier_name")
    private String carrierName;

    @Column(value = "delivery_type")
    @CassandraType(type = Name.TEXT)
    private DeliveryType deliveryType;

    @Column(value = "delivery_status")
    @CassandraType(type = Name.TEXT)
    @Builder.Default
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(value = "delivery_requirement")
    private String deliveryRequirement;

    @Column(value = "tracking_number")
    private String trackingNumber;

    @Column(value = "recipient_name")
    private String recipientName;

    @Column(value = "recipient_address")
    private String recipientAddress;

    @Column(value = "recipient_contact")
    private String recipientContact;

    @Override
    public UUID getId() {
        return deliveryId;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public static Delivery createFrom(DeliveryCreateRequestDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .deliveryRequirement(dto.getDeliveryRequirement())
                .recipientName(dto.getRecipientName())
                .recipientAddress(dto.getRecipientAddress())
                .recipientContact(dto.getRecipientContact())
                .build();
    }

    public void startDelivery(String carrierId, DeliveryType deliveryType,
            DeliveryStartRequestDto dto) {
        this.deliveryStatus = DeliveryStatus.IN_TRANSIT;
        this.carrierId = carrierId;
        this.deliveryType = deliveryType;
        this.carrierName = dto.getCarrierName();
        this.trackingNumber = dto.getTrackingNumber();
    }

}
