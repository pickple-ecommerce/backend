package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.domain.model.Delivery;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryRepository extends CassandraRepository<Delivery, UUID> {

}
