package com.urutare.stockm.dto.request;

import java.util.UUID;

import com.urutare.stockm.models.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignOrRemoveRoleBody {
    @Schema(description = "Role name", example = "ADMIN")
    private ERole name;

    @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;
}
