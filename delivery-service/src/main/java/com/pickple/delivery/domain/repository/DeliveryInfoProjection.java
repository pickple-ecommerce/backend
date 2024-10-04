package com.pickple.delivery.domain.repository;

import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.UUID;

public interface DeliveryInfoProjection {

    UUID getDeliveryId();

    UUID getOrderId();

    String getCarrierName();

    DeliveryType getDeliveryType();

    String getTrackingNumber();

    DeliveryStatus getDeliveryStatus();

    String getDeliveryRequirement();

    String getRecipientName();

    String getRecipientAddress();

    String getRecipientContact();
}
