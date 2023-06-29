package com.urutare.stockmcategory.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urutare.stockmcategory.common.StringUtil;
import com.urutare.stockmcategory.entity.Category;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.dto.CategoryDTO;
import com.urutare.stockmcategory.models.request.CategoryRequestBody;
import com.urutare.stockmcategory.models.response.PaginatedResponseDTO;
import com.urutare.stockmcategory.repository.CategoryRepository;
import com.urutare.stockmcategory.utils.CloudinaryUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/category-service/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categories", description = "Categories API")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CloudinaryUtil cloudinaryUtil;

    @GetMapping
    public PaginatedResponseDTO<CategoryDTO> getCategories(@RequestParam(required = false) UUID parentId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") @Max(25) int childrenLimit,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Category> categoryPage;

        if (keyword != null) {
            categoryPage = categoryRepository.searchByName(keyword, pageable);
        } else {
            categoryPage = categoryRepository.findByParentId(parentId, pageable);
        }

        List<CategoryDTO> categoryDTOs = CategoryDTO.mapCategoriesToDTOs(categoryPage.getContent(), childrenLimit);

        PaginatedResponseDTO<CategoryDTO> paginatedResponseDTO = new PaginatedResponseDTO<>();
        paginatedResponseDTO.setContent(categoryDTOs);
        paginatedResponseDTO.setPageNumber(categoryPage.getNumber());
        paginatedResponseDTO.setPageSize(categoryPage.getSize());
        paginatedResponseDTO.setTotalPages(categoryPage.getTotalPages());
        paginatedResponseDTO.setTotalElements(categoryPage.getTotalElements());
        paginatedResponseDTO.setFirst(categoryPage.isFirst());
        paginatedResponseDTO.setLast(categoryPage.isLast());

        return paginatedResponseDTO;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public Category getCategory(@PathVariable UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create category")
    public ResponseEntity<Category> createCategory(@ModelAttribute CategoryRequestBody categoryBody) {
        Category category = new Category();
        UUID parentId = categoryBody.getParentId();

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Parent Category not found"));
            category.setParent(parent);
        }

        if (categoryBody.getImage() != null) {
            try {
                String imageUrl = cloudinaryUtil.uploadImage(categoryBody.getImage());
                category.setImage(imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        category.setName(categoryBody.getName());
        Category newCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update category")
    public Category updateCategory(@ModelAttribute CategoryRequestBody categoryBody, @PathVariable UUID id) {
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

        if (categoryBody.getImage() != null) {
            try {
                String imageUrl = cloudinaryUtil.uploadImage(categoryBody.getImage());
                category.setImage(imageUrl);

                if (StringUtil.isNotNullOrEmpty(category.getImage())) {
                    cloudinaryUtil.deleteImage(category.getImage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (StringUtil.isNotNullOrEmpty(categoryBody.getName())) {
            category.setName(categoryBody.getName());
        }
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
        if (category.getImage() != null) {
            try {
                cloudinaryUtil.deleteImage(category.getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.noContent().build();
    }
}
