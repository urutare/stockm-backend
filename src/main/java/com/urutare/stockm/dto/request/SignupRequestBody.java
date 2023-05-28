package com.urutare.stockm.dto.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupRequestBody {
    @Schema(description = "Email", example = "kamana@urutare.com")
    String email;
    @Schema(description = "Password", example = "Kamana123")
    String password;
    @Schema(description = "Full name", example = "Jean Kamana")
    String fullName;
    @Schema(description = "Phone number", example = "250788000000")
    String phoneNumber;
    @Schema(description = "Roles", example = "[\"USER\"]")
    private Set<String> role;
    @Schema(description = "Username", example = "kamana")
    private String username;

    public SignupRequestBody(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }
}
