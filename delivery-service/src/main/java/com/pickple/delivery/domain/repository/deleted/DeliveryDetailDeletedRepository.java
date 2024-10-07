package com.pickple.delivery.domain.repository.deleted;

import com.pickple.delivery.domain.model.deleted.DeliveryDetailDeleted;
import java.util.List;

public interface DeliveryDetailDeletedRepository {

    <S extends DeliveryDetailDeleted> List<S> saveAll(Iterable<S> entities);

}
