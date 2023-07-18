package com.urutare.stockmcategory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
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
