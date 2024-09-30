package com.pickple.delivery.domain.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyClass
public class DeliveryDetailId implements Serializable {

    @PrimaryKeyColumn(name = "delivery_id", type = PrimaryKeyType.PARTITIONED)
    private UUID deliveryId;

    @PrimaryKeyColumn(name = "delivery_detail_time", type = PrimaryKeyType.CLUSTERED)
    private Instant deliveryDetailTime;
}
