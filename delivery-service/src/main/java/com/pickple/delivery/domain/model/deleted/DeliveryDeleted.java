package com.pickple.delivery.domain.model.deleted;

import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "p_deliveries_deleted")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDeleted {

    @Id
    private UUID deliveryId;

    @Field("order_id")
    private UUID orderId;

    @Field("carrier_id")
    private String carrierId;

    @Field("carrier_name")
    private String carrierName;

    @Field("delivery_type")
    private DeliveryType deliveryType;

    @Field("delivery_status")
    private DeliveryStatus deliveryStatus;

    @Field("delivery_requirement")
    private String deliveryRequirement;

    @Field("tracking_number")
    private String trackingNumber;

    @Field("recipient_name")
    private String recipientName;

    @Field("recipient_address")
    private String recipientAddress;

    @Field("recipient_contact")
    private String recipientContact;

    @Field("created_at")
    private Date createdAt;

    @Field("updated_at")
    private Date updatedAt;

    @Field("created_by")
    private String createdBy;

    @Field("updated_by")
    private String updatedBy;

    @Field("deleted_at")
    protected Instant deletedAt;

    @Field("deleted_by")
    protected String deletedBy;

    @Field("delivery_details")
    private List<DeliveryDetailDeleted> deliveryDetails;

    public void delete(String deleter) {
        this.deletedAt = Instant.now();
        this.deletedBy = deleter;
    }

    public static DeliveryDeleted fromDelivery(Delivery delivery, List<DeliveryDetailDeleted> details) {
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
                .deliveryDetails(details)
                .build();
    }
}
