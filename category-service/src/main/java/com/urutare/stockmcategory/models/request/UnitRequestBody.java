package com.urutare.stockmcategory.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UnitRequestBody {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String symbol;

    @NotBlank
    @Size(max = 50)
    private String category;

    @NotNull
    @Positive
    private Double conversionFactor;

    @Size(max = 255)
    private String description;
}
