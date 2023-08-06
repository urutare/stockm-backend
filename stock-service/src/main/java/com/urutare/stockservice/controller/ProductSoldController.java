package com.urutare.stockservice.controller;

import com.urutare.stockservice.aspect.RequiresRole;
import com.urutare.stockservice.entities.ProductSold;
import com.urutare.stockservice.models.enums.UserRole;
import com.urutare.stockservice.repository.ProductSoldRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductSoldController {
    private final ProductSoldRepository productSoldRepository;
    private final HttpServletRequest request;

    @QueryMapping
    @RequiresRole(UserRole.ADMIN)
    public ProductSold productSoldById(@Argument UUID id) {
        UUID userId = UUID.fromString(request.getHeader("userId"));
        System.out.println("userId = " + userId);
        return productSoldRepository.findById(id).orElse(null);
    }

}
