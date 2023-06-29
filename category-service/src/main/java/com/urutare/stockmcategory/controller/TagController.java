package com.urutare.stockmcategory.controller;

import com.urutare.stockmcategory.entity.ProductTag;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.dto.TagDTO;
import com.urutare.stockmcategory.models.request.TagRequestBody;
import com.urutare.stockmcategory.repository.TagRepository;
import com.urutare.stockmcategory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category-service/tags")
@Tag(name = "Tags", description = "Tags API")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;
    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by id")
    public TagDTO getTag(@PathVariable UUID id) {
        ProductTag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        return TagDTO.fromTag(tag);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create tag")
    public ResponseEntity<TagDTO> createTag(@RequestBody @Valid TagRequestBody tagBody) {
        ProductTag tag = new ProductTag();
        tag.setName(tagBody.getName());
        tag.setProduct(productService.getProductById(tagBody.getProductId()).orElse(null));

        ProductTag createdTag = tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(TagDTO.fromTag(createdTag));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update tag")
    public TagDTO updateTag(@PathVariable UUID id, @RequestBody @Valid TagRequestBody tagBody) {
        ProductTag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        tag.setName(tagBody.getName());
        tag.setProduct(productService.getProductById(tagBody.getProductId()).orElse(null));

        ProductTag updatedTag = tagRepository.save(tag);
        return TagDTO.fromTag(updatedTag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete tag")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        ProductTag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        tagRepository.delete(tag);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get tags by product ID")
    public List<TagDTO> getTagsByProduct(@PathVariable UUID productId) {
        List<ProductTag> tags = tagRepository.findByProductId(productId);
        return TagDTO.fromTags(tags);
    }
}
