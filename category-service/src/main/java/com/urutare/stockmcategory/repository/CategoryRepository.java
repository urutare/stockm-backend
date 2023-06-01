package com.urutare.stockmcategory.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmcategory.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentId(UUID parentId);
}
