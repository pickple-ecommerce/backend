package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

}
