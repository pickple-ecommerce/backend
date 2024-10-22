package com.pickple.delivery;

import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.infrastructure.config.AuditorConfig;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(AuditorConfig.class)
public class DeliveryMongoRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Delivery.class);
        mongoTemplate.dropCollection(DeliveryDetail.class);
    }

    @Test
    public void testSaveAndFindDelivery() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        List<DeliveryDetail> deliveryDetails = Arrays.asList(
                DeliveryDetail.builder()
                        .deliveryDetailTime(Date.from(Instant.now()))
                        .deliveryDetailStatus("간선 상차")
                        .deliveryDetailDescription("인천 HUB에서 간선 상차 완료")
                        .build(),
                DeliveryDetail.builder()
                        .deliveryDetailTime(Date.from(Instant.now().plusSeconds(1)))
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
                .deliveryDetails(deliveryDetails)  // Embedded list of delivery details in the Delivery object
                .build();

        // When
        deliveryMongoRepository.save(delivery);

        // Then
        Delivery foundDelivery = deliveryMongoRepository.findById(deliveryId).orElse(null);
        assertThat(foundDelivery).isNotNull();
        assertThat(foundDelivery.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(foundDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PENDING);

        // Validate DeliveryDetails
        List<DeliveryDetail> foundDetails = foundDelivery.getDeliveryDetails();
        assertThat(foundDetails).hasSize(2);
        assertThat(foundDetails.get(0).getDeliveryDetailStatus()).isEqualTo("간선 상차");
        assertThat(foundDetails.get(0).getDeliveryDetailDescription()).isEqualTo("인천 HUB에서 간선 상차 완료");
        assertThat(foundDetails.get(1).getDeliveryDetailStatus()).isEqualTo("간선 하차");
    }
}