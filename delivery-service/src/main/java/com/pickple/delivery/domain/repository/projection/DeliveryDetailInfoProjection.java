package com.pickple.delivery.domain.repository.projection;

import com.pickple.delivery.domain.model.DeliveryDetailId;

public interface DeliveryDetailInfoProjection {

    DeliveryDetailId getDeliveryDetailId();

    String getDeliveryDetailStatus();

    String getDeliveryDetailDescription();
}
