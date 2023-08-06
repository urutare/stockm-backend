package com.urutare.stockservice.repository;

import com.urutare.stockservice.entities.ProductSold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductSoldRepository extends JpaRepository<ProductSold, UUID> {
}
