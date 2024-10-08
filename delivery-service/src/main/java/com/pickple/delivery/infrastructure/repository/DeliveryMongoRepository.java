package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.model.Delivery;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryMongoRepository extends DeliveryRepository,
        MongoRepository<Delivery, UUID> {

    @Override
    <S extends Delivery> @Nonnull S save(@Nonnull S entity);

    @Override
    @Nonnull Optional<Delivery> findById(@Nonnull UUID deliveryId);

    @Override
    boolean existsById(@Nonnull UUID deliveryId);

    @Override
    void deleteById(@Nonnull UUID deliveryId);

    @Override
    @Nonnull
    Page<Delivery> findAll(@Nonnull Pageable pageable);

    Page<Delivery> findByCarrierName(String carrier, Pageable pageable);

    Optional<Delivery> findByTrackingNumber(String trackingNumber);

    Page<Delivery> findByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    Page<Delivery> findByDeliveryType(DeliveryType deliveryType, Pageable pageable);
}