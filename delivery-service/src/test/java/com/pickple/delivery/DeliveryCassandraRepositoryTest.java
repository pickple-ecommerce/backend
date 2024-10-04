package com.pickple.delivery;

import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.DeliveryDetailId;
import com.pickple.delivery.infrastructure.config.AuditorConfig;
import com.pickple.delivery.domain.repository.DeliveryDetailRepository;
import com.pickple.delivery.infrastructure.repository.DeliveryCassandraRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataCassandraTest
@ExtendWith(SpringExtension.class)
@Import(AuditorConfig.class)
public class DeliveryCassandraRepositoryTest {

    @Autowired
    private DeliveryCassandraRepository deliveryCassandraRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @BeforeEach
    public void setUp() {
        cassandraTemplate.truncate(Delivery.class);
        cassandraTemplate.truncate(DeliveryDetail.class);
    }

    @Test
    public void testSaveAndFindDelivery() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        List<DeliveryDetail> deliveryDetails = Arrays.asList(
                DeliveryDetail.builder()
                        .deliveryDetailId(DeliveryDetailId.builder()
                                .deliveryId(deliveryId)
                                .deliveryDetailTime(Instant.now())
                                .build())
                        .deliveryDetailStatus("간선 상차")
                        .deliveryDetailDescription("인천 HUB에서 간선 상차 완료")
                        .build(),
                DeliveryDetail.builder()
                        .deliveryDetailId(DeliveryDetailId.builder()
                                .deliveryId(deliveryId)
                                .deliveryDetailTime(Instant.now().plusSeconds(1))
                                .build())
                        .deliveryDetailStatus("간선 하차")
                        .deliveryDetailDescription("경기도 HUB에서 간선 하차 완료")
                        .build()
        );

        Delivery delivery = Delivery.builder()
                .deliveryId(deliveryId)
                .orderId(orderId)
                .carrierId("HK4")
                .deliveryType(DeliveryType.COURIER)
                .deliveryStatus(DeliveryStatus.PENDING)
                .deliveryRequirement("문 앞에 두고 가주세요.")
                .carrierName("스프링 택배")
                .trackingNumber("DO123748759KC")
                .recipientName("김개미")
                .recipientAddress("허리도 가늘군 만지면 부러지리")
                .recipientContact("010-1234-5678")
                .build();

        // When
        deliveryCassandraRepository.save(delivery);
        deliveryDetailRepository.saveAll(deliveryDetails);

        // Then
        Delivery foundDelivery = deliveryCassandraRepository.findById(deliveryId).orElse(null);
        assertThat(foundDelivery).isNotNull();
        assertThat(foundDelivery.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(foundDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PENDING);

        // Validate DeliveryDetails
        List<DeliveryDetail> foundDetails = deliveryDetailRepository.findByDeliveryDetailIdDeliveryId(deliveryId);
        assertThat(foundDetails).hasSize(2);
        assertThat(foundDetails.get(0).getDeliveryDetailStatus()).isEqualTo("간선 상차");
        assertThat(foundDetails.get(0).getDeliveryDetailDescription()).isEqualTo("인천 HUB에서 간선 상차 완료");
        assertThat(foundDetails.get(1).getDeliveryDetailStatus()).isEqualTo("간선 하차");
    }
}