package com.pickple.delivery.domain.repository;

import com.pickple.delivery.domain.model.DeliveryDetail;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface DeliveryDetailRepository {

    List<DeliveryDetail> findByDeliveryDetailIdDeliveryId(UUID deliveryId);

    <S extends DeliveryDetail> S save(S entity);

    <S extends DeliveryDetail> List<S> saveAll(Iterable<S> entities);
}
