package com.urutare.stockmcategory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urutare.stockmcategory.entity.Category;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.request.CategoryRequestBody;
import com.urutare.stockmcategory.repository.CategoryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categories", description = "Categories API")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategories() {
        // if (categoryId != null) {
        // return categoryRepository.findByParentId(categoryId);
        // }
        return categoryRepository.findByParentId(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public Category getCategory(@PathVariable UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequestBody categoryBody) {
        Category category = new Category();
        UUID parentId = categoryBody.getParentId();

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Parent Category not found"));
            category.setParent(parent);
        }
        category.setName(categoryBody.getName());
        Category newCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update category")
    public Category updateCategory(@RequestBody CategoryRequestBody categoryBody, @PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        UUID parentId = categoryBody.getParentId();

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Parent Category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        category.setName(categoryBody.getName());
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
