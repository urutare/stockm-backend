package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.urutare.stockmcategory.entity.TaxType;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestBody {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotBlank(message = "Measurement type is required")
    private String measurementType;

    @NotNull(message = "Tax type is required")
    private TaxType taxType;

    @NotNull(message = "Quantity alert is required")
    private Double quantityAlert;

    @NotBlank(message = "Bar code is required")
    private String barCode;

    @NotBlank(message = "Description is required")
    private String description;

    private MultipartFile image;
}
