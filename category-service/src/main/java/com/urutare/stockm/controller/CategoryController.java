package com.urutare.stockm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urutare.stockm.entity.Category;
import com.urutare.stockm.exception.NotFoundException;
import com.urutare.stockm.models.CategoryRequestBody;
import com.urutare.stockm.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getCategories() {
        // if (categoryId != null) {
        //     return categoryRepository.findByParentId(categoryId);
        // }
        return categoryRepository.findByParentId(null);
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequestBody categoryBody) {
        Category category = new Category();
        Long parentId = categoryBody.getParentId();

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
    public Category updateCategory(@RequestBody CategoryRequestBody categoryBody, @PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Long parentId = categoryBody.getParentId();

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
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
