package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.DeliveryDetailId;
import com.pickple.delivery.domain.repository.DeliveryDetailInfoProjection;
import com.pickple.delivery.domain.repository.DeliveryDetailRepository;
import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryDetailCassandraRepository extends
        CassandraRepository<DeliveryDetail, DeliveryDetailId>, DeliveryDetailRepository {

    List<DeliveryDetail> findByDeliveryDetailIdDeliveryId(UUID deliveryId);

    @Override
    @Nonnull
    <S extends DeliveryDetail> S save(@Nonnull S entity);

    @Override
    @Nonnull
    <S extends DeliveryDetail> List<S> saveAll(@Nonnull Iterable<S> entities);

    Collection<DeliveryDetailInfoProjection> findInfoByDeliveryDetailIdDeliveryId(UUID deliveryId);

}
