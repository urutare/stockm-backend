package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import com.urutare.stockmcategory.models.enums.Unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PieceRequestBody {

    @NotBlank
    private String name;

    @NotNull
    private int numberOfPieces;

    private UUID parentPieceId;

    @NotNull
    private Unit unit;

    @NotNull
    private UUID productId;
}
