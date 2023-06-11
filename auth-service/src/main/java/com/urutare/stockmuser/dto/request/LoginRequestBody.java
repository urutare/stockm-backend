package com.urutare.stockmuser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestBody {
    @Schema(description = "Username or email", example = "admin")
    private String username;

    @Schema(description = "Password", example = "Admin123")
    private String password;
}
