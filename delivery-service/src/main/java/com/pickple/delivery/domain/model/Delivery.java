package com.pickple.delivery.domain.model;

import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.request.DeliveryUpdateRequestDto;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document(collection = "p_deliveries")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity implements Persistable<UUID> {

    @Id
    @Field("delivery_id")
    @Builder.Default
    private UUID deliveryId = UUID.randomUUID();

    @Field("order_id")
    private UUID orderId;

    @Field("carrier_id")
    private String carrierId;

    @Field("carrier_name")
    private String carrierName;

    @Field("delivery_type")
    private DeliveryType deliveryType;

    @Field("delivery_status")
    @Builder.Default
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

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

    @Field("delivery_details")
    @Builder.Default
    private List<DeliveryDetail> deliveryDetails = new ArrayList<>();

    @Field("is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    public static Delivery createFrom(DeliveryCreateRequestDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .deliveryRequirement(dto.getDeliveryRequirement())
                .recipientName(dto.getRecipientName())
                .recipientAddress(dto.getRecipientAddress())
                .recipientContact(dto.getRecipientContact())
                .build();
    }

    public void startDelivery(String carrierId, DeliveryType deliveryType, DeliveryStartRequestDto dto) {
        this.deliveryStatus = DeliveryStatus.IN_TRANSIT;
        this.carrierId = carrierId;
        this.deliveryType = deliveryType;
        this.carrierName = dto.getDeliveryCarrier().getCompanyName();
        this.trackingNumber = dto.getTrackingNumber();
    }

    public void addDeliveryDetail(DeliveryDetail detail) {
        this.deliveryDetails.add(detail);
    }

    public void updateDelivery(DeliveryUpdateRequestDto dto) {
        this.deliveryStatus = dto.getDeliveryStatus();
        this.deliveryType = dto.getDeliveryType();
        this.deliveryRequirement = dto.getDeliveryRequirement();
        this.trackingNumber = dto.getTrackingNumber();
        this.recipientName = dto.getRecipientName();
        this.recipientAddress = dto.getRecipientAddress();
        this.recipientContact = dto.getRecipientContact();
    }

    @Override
    public UUID getId() {
        return deliveryId;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}