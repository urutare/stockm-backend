package com.urutare.stockmuser.dto.request;

import com.urutare.stockmuser.models.ERole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleBody {
    @Schema(description = "Role name", example = "ADMIN")
    private ERole name;
    private UUID createdBy;
    private UUID updatedBy;

}