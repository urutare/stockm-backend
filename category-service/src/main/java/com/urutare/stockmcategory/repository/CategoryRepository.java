package com.urutare.stockmcategory.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmcategory.entity.Category;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentId(UUID parentId);

    Page<Category> findByParentId(UUID parentId, Pageable pageable);
}
