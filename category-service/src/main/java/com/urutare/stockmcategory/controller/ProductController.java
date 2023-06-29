package com.urutare.stockmcategory.controller;

import com.urutare.stockmcategory.entity.Product;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.dto.ProductDTO;
import com.urutare.stockmcategory.models.request.ProductRequestBody;
import com.urutare.stockmcategory.models.response.PaginatedResponseDTO;
import com.urutare.stockmcategory.service.ProductService;
import com.urutare.stockmcategory.utils.CloudinaryUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category-service/products")
@Tag(name = "Products", description = "Products API")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;
    private final CloudinaryUtil cloudinaryUtil;

    @Autowired
    public ProductController(ProductService productService, CloudinaryUtil cloudinaryUtil) {
        this.productService = productService;
        this.cloudinaryUtil = cloudinaryUtil;
    }

    @GetMapping
    public PaginatedResponseDTO<ProductDTO> getProducts(@RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Product> productPage;

        if (keyword != null) {
            productPage = productService.searchProductsByName(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategoryId(categoryId, pageable);
        } else {
            productPage = productService.getAllProducts(pageable);
        }

        List<ProductDTO> productDTOs = ProductDTO.mapProductsToDTOs(productPage.getContent());

        PaginatedResponseDTO<ProductDTO> paginatedResponseDTO = new PaginatedResponseDTO<>();
        paginatedResponseDTO.setContent(productDTOs);
        paginatedResponseDTO.setPageNumber(productPage.getNumber());
        paginatedResponseDTO.setPageSize(productPage.getSize());
        paginatedResponseDTO.setTotalPages(productPage.getTotalPages());
        paginatedResponseDTO.setTotalElements(productPage.getTotalElements());
        paginatedResponseDTO.setFirst(productPage.isFirst());
        paginatedResponseDTO.setLast(productPage.isLast());

        return paginatedResponseDTO;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public ProductDTO getProduct(@PathVariable UUID id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return ProductDTO.fromProduct(product);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create product")
    public ResponseEntity<ProductDTO> createProduct(@ModelAttribute ProductRequestBody productBody) {
        try {
            String imageUrl = null;
            if (productBody.getImage() != null) {
                imageUrl = cloudinaryUtil.uploadImage(productBody.getImage());
            }

            Product product = new Product();
            product.setName(productBody.getName());
            product.setCategory(productService.getCategoryById(productBody.getCategoryId()));
            product.setMeasurementType(productBody.getMeasurementType());
            product.setTaxType(productBody.getTaxType());
            product.setBarCode(productBody.getBarCode());
            product.setDescription(productBody.getDescription());
            product.setImage(imageUrl);

            productService.createProduct(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDTO.fromProduct(product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update product")
    public ProductDTO updateProduct(@ModelAttribute @Valid ProductRequestBody productBody,
            @PathVariable UUID id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(productBody.getName());
        product.setCategory(productService.getCategoryById(productBody.getCategoryId()));
        product.setMeasurementType(productBody.getMeasurementType());
        product.setTaxType(productBody.getTaxType());
        product.setBarCode(productBody.getBarCode());
        product.setDescription(productBody.getDescription());

        MultipartFile image = productBody.getImage();

        if (image != null) {
            try {
                String imageUrl = cloudinaryUtil.uploadImage(image);

                if (product.getImage() != null) {
                    cloudinaryUtil.deleteImage(product.getImage());
                }

                product.setImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Product updatedProduct = productService.updateProduct(product);
        return ProductDTO.fromProduct(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete product")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (cloudinaryUtil.isValidUrl(product.getImage())) {
            try {
                cloudinaryUtil.deleteImage(product.getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.deleteProduct(product);
        return ResponseEntity.noContent().build();
    }
}
