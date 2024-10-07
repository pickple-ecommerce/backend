package com.pickple.delivery.infrastructure.repository.deleted;

import com.pickple.delivery.domain.model.DeliveryDetailId;
import com.pickple.delivery.domain.model.deleted.DeliveryDetailDeleted;
import com.pickple.delivery.domain.repository.deleted.DeliveryDetailDeletedRepository;
import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryDetailDeletedCassandraRepository extends DeliveryDetailDeletedRepository,
        CassandraRepository<DeliveryDetailDeleted, DeliveryDetailId> {

    @Override
    @Nonnull
    <S extends DeliveryDetailDeleted> List<S> saveAll(@Nonnull Iterable<S> entities);
}
