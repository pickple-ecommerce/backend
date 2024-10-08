package com.pickple.delivery.infrastructure.repository.deleted;

import com.pickple.delivery.domain.model.deleted.DeliveryDeleted;
import com.pickple.delivery.domain.repository.deleted.DeliveryDeletedRepository;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryDeletedMongoRepository extends DeliveryDeletedRepository,
        MongoRepository<DeliveryDeleted, UUID> {

    @Override
    @Nonnull
    <S extends DeliveryDeleted> S save(@Nonnull S entity);

}
