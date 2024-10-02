package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
}