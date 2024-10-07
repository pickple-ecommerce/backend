package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.model.Delivery;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryCassandraRepository extends DeliveryRepository, CassandraRepository<Delivery, UUID> {

    @Override
    <S extends Delivery> @Nonnull S save(@Nonnull S entity);

    @Override
    @Nonnull Optional<Delivery> findById(@Nonnull UUID deliveryId);

    @Override
    boolean existsById(@Nonnull UUID deliveryId);

    @Override
    void deleteById(@Nonnull UUID deliveryId);
}