package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import lombok.Data;

@Data
public class TagRequestBody {
    private String name;
    private UUID productId;
}
