package com.urutare.stockmcategory.repository;

import com.urutare.stockmcategory.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    // You can define custom query methods or use the ones provided by JpaRepository
}
