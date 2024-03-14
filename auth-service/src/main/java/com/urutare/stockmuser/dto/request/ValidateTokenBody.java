package com.urutare.stockmuser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenBody {
    @Schema(description = "Access token", example = "")
    String accessToken;

    @Schema(description = "Retrieve user data", example = "false")
    boolean retrieveUserData = false;
}
