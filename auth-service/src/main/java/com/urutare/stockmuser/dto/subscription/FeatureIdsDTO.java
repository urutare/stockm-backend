package com.urutare.stockmuser.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FeatureIdsDTO {
    @Schema(description = "Feature ids", example = "[\"e3b6c5e3-4f0e-4b7a-8e1a-0e2e8f5b6e0a\",\"e3b6c5e3-4f0e-4b7a-8e1a-0e2e8f5b6e0a\"]")
    private UUID[] featureIds;
}
