package com.urutare.stockmcategory.models;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoryRequestBody {
    private String name;
    private UUID parentId;
}
