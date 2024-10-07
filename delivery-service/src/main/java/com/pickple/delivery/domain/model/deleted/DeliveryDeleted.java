package com.pickple.delivery.domain.model.deleted;

import com.pickple.delivery.domain.model.Delivery;
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

@Table("p_deliveries_deleted")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDeleted {

    @PrimaryKey(value = "delivery_id")
    @CassandraType(type = Name.UUID)
    private UUID deliveryId;

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
    private DeliveryStatus deliveryStatus;

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

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    protected Instant deletedAt;

    @Column("deleted_by")
    protected String deletedBy;

    public void delete(String deleter) {
        this.deletedAt = Instant.now();
        this.deletedBy = deleter;
    }

    public static DeliveryDeleted fromDelivery(Delivery delivery) {
        return DeliveryDeleted.builder()
                .deliveryId(delivery.getDeliveryId())
                .carrierName(delivery.getCarrierName())
                .deliveryType(delivery.getDeliveryType())
                .trackingNumber(delivery.getTrackingNumber())
                .orderId(delivery.getOrderId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .deliveryRequirement(delivery.getDeliveryRequirement())
                .recipientName(delivery.getRecipientName())
                .recipientAddress(delivery.getRecipientAddress())
                .recipientContact(delivery.getRecipientContact())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .build();
    }

}