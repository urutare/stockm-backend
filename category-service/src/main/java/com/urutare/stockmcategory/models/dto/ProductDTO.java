package com.urutare.stockmcategory.models.dto;

import com.urutare.stockmcategory.entity.Product;
import com.urutare.stockmcategory.models.enums.Unit;

import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ProductDTO {
    private UUID id;
    private String name;
    private CategoryDTO category;
    private Unit measurementType;
    private String taxType;
    private String barCode;
    private String description;
    private String imageUrl;

    public static ProductDTO fromProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategory(CategoryDTO.fromCategory(product.getCategory()));
        productDTO.setMeasurementType(product.getMeasurementType());
        productDTO.setTaxType(product.getTaxType().toString());
        productDTO.setBarCode(product.getBarCode());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageUrl(product.getImage());
        return productDTO;
    }

    public static List<ProductDTO> fromProducts(List<Product> products) {
        return products.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
    }

    public static List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        return products.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
    }
}
