package com.urutare.stockservice.models.input;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInput {
    private String name;
    private UUID categoryId;
    private String description;
    private List<UUID> images;
    private Double taxRate;
    private List<UUID> tagIds;
    private String unitClass;
    private String barcode;
    private UUID pieceId;
    private Double lowQuantity;
    private Boolean lowStockAlert;
}
