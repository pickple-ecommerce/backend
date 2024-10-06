package com.pickple.delivery.domain.repository;

import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.repository.projection.DeliveryDetailInfoProjection;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DeliveryDetailRepository {

    List<DeliveryDetail> findByDeliveryDetailIdDeliveryId(UUID deliveryId);

    <S extends DeliveryDetail> S save(S entity);

    <S extends DeliveryDetail> List<S> saveAll(Iterable<S> entities);

    Collection<DeliveryDetailInfoProjection> findInfoByDeliveryDetailIdDeliveryIdAndIsDeleteFalse(UUID deliveryId);
}
