package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.PreOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PreOrderRepository extends JpaRepository<PreOrderDetails, UUID> {
}
