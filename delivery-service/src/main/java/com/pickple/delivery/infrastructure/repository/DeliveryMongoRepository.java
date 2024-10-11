package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
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
import org.springframework.data.mongodb.repository.Query;

public interface DeliveryMongoRepository extends DeliveryRepository,
        MongoRepository<Delivery, UUID> {

    @Override
    <S extends Delivery> @Nonnull S save(@Nonnull S entity);

    @Nonnull Optional<Delivery> findById(@Nonnull UUID deliveryId);

    @Override
    boolean existsById(@Nonnull UUID deliveryId);

    @Override
    void deleteById(@Nonnull UUID deliveryId);

    @Query(value = "{}", fields = "{ 'deliveryId' : 1, 'orderId' : 1, 'carrierName' : 1, 'deliveryType' : 1, 'trackingNumber' : 1, 'deliveryStatus' : 1, 'deliveryRequirement' : 1, 'recipientName' : 1, 'recipientAddress' : 1, 'recipientContact' : 1, 'deliveryDetails' : 1 }")
    Page<DeliveryInfoResponseDto> findInfoAll(Pageable pageable);

    @Query(value = "{ 'carrierName' : ?0 }", fields = "{ 'deliveryId' : 1, 'orderId' : 1, 'carrierName' : 1, 'deliveryType' : 1, 'trackingNumber' : 1, 'deliveryStatus' : 1, 'deliveryRequirement' : 1, 'recipientName' : 1, 'recipientAddress' : 1, 'recipientContact' : 1, 'deliveryDetails' : 1 }")
    Page<DeliveryInfoResponseDto> findByCarrierName(String carrierName, Pageable pageable);

    @Query(value = "{ 'trackingNumber' : ?0 }", fields = "{ 'deliveryId' : 1, 'orderId' : 1, 'carrierName' : 1, 'deliveryType' : 1, 'trackingNumber' : 1, 'deliveryStatus' : 1, 'deliveryRequirement' : 1, 'recipientName' : 1, 'recipientAddress' : 1, 'recipientContact' : 1, 'deliveryDetails' : 1 }")
    Optional<DeliveryInfoResponseDto> findByTrackingNumber(String trackingNumber);

    @Query(value = "{ 'deliveryStatus' : ?0 }", fields = "{ 'deliveryId' : 1, 'orderId' : 1, 'carrierName' : 1, 'deliveryType' : 1, 'trackingNumber' : 1, 'deliveryStatus' : 1, 'deliveryRequirement' : 1, 'recipientName' : 1, 'recipientAddress' : 1, 'recipientContact' : 1, 'deliveryDetails' : 1 }")
    Page<DeliveryInfoResponseDto> findByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    @Query(value = "{ 'deliveryType' : ?0 }", fields = "{ 'deliveryId' : 1, 'orderId' : 1, 'carrierName' : 1, 'deliveryType' : 1, 'trackingNumber' : 1, 'deliveryStatus' : 1, 'deliveryRequirement' : 1, 'recipientName' : 1, 'recipientAddress' : 1, 'recipientContact' : 1, 'deliveryDetails' : 1 }")
    Page<DeliveryInfoResponseDto> findByDeliveryType(DeliveryType deliveryType, Pageable pageable);

}