package com.pickple.delivery.application.port;

import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface OrderClient {

    String getUsernameByDeliveryId(
            @PathVariable("deliveryId") UUID deliveryId,
            @RequestHeader("X-User-Roles") String role,
            @RequestHeader("X-User-Name") String username
    );

}
