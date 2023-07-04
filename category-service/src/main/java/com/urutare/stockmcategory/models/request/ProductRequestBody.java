package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.urutare.stockmcategory.entity.TaxType;
import com.urutare.stockmcategory.models.enums.Unit;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestBody {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotBlank(message = "Measurement type is required")
    private Unit measurementType;

    @NotNull(message = "Tax type is required")
    private TaxType taxType;

    private String barCode;

    private String description;

    private MultipartFile image;
}
