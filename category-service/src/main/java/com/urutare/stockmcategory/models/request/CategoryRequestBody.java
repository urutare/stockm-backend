package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryRequestBody {
    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "Parent Category ID", example = "d0b0c9e0-8b7a-4e1e-9b0e-1b9b6e6f9b0e")
    private UUID parentId;
}
