package com.pickple.delivery.domain.repository;

import com.pickple.delivery.domain.model.Delivery;

public interface DeliveryRepository {

    <S extends Delivery> S save(S entity);

}