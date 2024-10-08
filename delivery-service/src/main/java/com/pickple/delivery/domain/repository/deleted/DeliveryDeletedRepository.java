package com.pickple.delivery.domain.repository.deleted;

import com.pickple.delivery.domain.model.deleted.DeliveryDeleted;

public interface DeliveryDeletedRepository {

    <S extends DeliveryDeleted> S save(S entity);

}
