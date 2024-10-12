package com.pickple.delivery.domain.repository;

import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

    <S extends Delivery> S save(S entity);

    Optional<Delivery> findById(UUID deliveryId);

    Optional<DeliveryInfoResponseDto> findByTrackingNumber(String trackingNumber);

    boolean existsById(@Nonnull UUID deliveryId);

    void deleteById(UUID deliveryId);

    Page<Delivery> findAll(Pageable pageable);

    Page<DeliveryInfoResponseDto> findInfoAll(Pageable pageable);

    Page<DeliveryInfoResponseDto> findByCarrierName(String carrier, Pageable pageable);

    Page<DeliveryInfoResponseDto> findByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    Page<DeliveryInfoResponseDto> findByDeliveryType(DeliveryType deliveryType, Pageable pageable);

}