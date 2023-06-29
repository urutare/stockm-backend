package com.urutare.stockmcategory.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PieceRequestBody {
    @NotNull
    private int numberOfPieces;

    private UUID parentPieceId;

    @NotNull
    private UUID unitId;

    @NotNull
    private UUID productId;
}
