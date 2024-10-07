package com.pickple.delivery.domain.repository;

import com.pickple.delivery.domain.model.Delivery;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {

    <S extends Delivery> S save(S entity);

    Optional<Delivery> findById(UUID deliveryId);

    boolean existsById(@Nonnull UUID deliveryId);

    void deleteById(UUID deliveryId);

}