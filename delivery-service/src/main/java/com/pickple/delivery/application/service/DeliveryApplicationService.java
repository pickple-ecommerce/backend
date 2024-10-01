package com.pickple.delivery.application.service;

import com.pickple.delivery.application.dto.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryCreateResponseDto;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.service.DeliveryDomainService;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryDomainService deliveryDomainService;

    public DeliveryCreateResponseDto createDelivery(
            DeliveryCreateRequestEvent deliveryCreateRequestEvent) {
        DeliveryCreateRequestDto deliveryCreateRequestDto = DeliveryCreateRequestDto.fromEvent(
                deliveryCreateRequestEvent);

        // TODO: DTO Validator 구현
        Delivery savedDelivery = deliveryRepository.save(
                deliveryDomainService.createDelivery(deliveryCreateRequestDto));
        return DeliveryCreateResponseDto.fromEntity(savedDelivery);
    }

}
