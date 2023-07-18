package com.urutare.stockmcategory.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.urutare.stockmcategory.entity.Category;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentId(UUID parentId);

    Page<Category> findByParentId(UUID parentId, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Category> searchByName(@Param("keyword") String keyword, Pageable pageable);
}
