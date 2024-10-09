package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.repository.PreOrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreOrderService {

    private final PreOrderRepository preOrderRepository;
    private final ProductRepository productRepository;

}
