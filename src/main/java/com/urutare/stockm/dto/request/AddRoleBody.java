package com.urutare.stockm.dto.request;

import com.urutare.stockm.models.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleBody {
    @Schema(description = "Role name", example = "ADMIN")
    private ERole name;

}