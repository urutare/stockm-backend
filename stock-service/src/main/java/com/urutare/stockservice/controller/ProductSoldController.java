package com.urutare.stockservice.controller;

import com.urutare.stockservice.entities.ProductSold;
import com.urutare.stockservice.repository.ProductSoldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductSoldController {
    private final ProductSoldRepository productSoldRepository;

    @QueryMapping
    public ProductSold productSoldById(@Argument UUID id) {
        return productSoldRepository.findById(id).orElse(null);
    }

}
