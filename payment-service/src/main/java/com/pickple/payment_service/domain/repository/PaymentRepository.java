package com.pickple.payment_service.domain.repository;

import com.pickple.payment_service.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByOrderIdAndIsDeleteIsFalse(UUID orderId);
}
