package com.urutare.stockmuser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenBody {
    @Schema(description = "Refresh token", example = "")
    String refreshToken;
}
