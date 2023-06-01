package com.urutare.stockm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockm.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);
}
