package com.pickple.delivery.infrastructure.repository.deleted;

import com.pickple.delivery.domain.model.deleted.DeliveryDeleted;
import com.pickple.delivery.domain.repository.deleted.DeliveryDeletedRepository;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryDeletedCassandraRepository extends DeliveryDeletedRepository,
        CassandraRepository<DeliveryDeleted, UUID> {

    @Override
    @Nonnull
    <S extends DeliveryDeleted> S save(@Nonnull S entity);

}
