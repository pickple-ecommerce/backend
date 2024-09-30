package com.pickple.delivery.infrastructure.repository;

import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.DeliveryDetailId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryDetailRepository extends CassandraRepository<DeliveryDetail, DeliveryDetailId> {

    List<DeliveryDetail> findByDeliveryDetailIdDeliveryId(UUID deliveryId);

}
