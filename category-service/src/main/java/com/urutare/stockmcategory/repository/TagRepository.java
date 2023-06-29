package com.urutare.stockmcategory.repository;

import com.urutare.stockmcategory.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<ProductTag, UUID> {
    List<ProductTag> findByProductId(UUID productId);
}
