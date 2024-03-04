package com.urutare.stockmuser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequestBody {
    @Schema(description = "Email", example = "kamana@urutare.com")
    String email;
    @Schema(description = "Password", example = "Kamana123")
    String password;
    @Schema(description = "First name", example = "Jean")
    String firstName;
    @Schema(description = "Last name", example = "Kamana")
    String lastName;
    @Schema(description = "Phone number", example = "250788000000")
    String phoneNumber;
    @Schema(description = "Roles", example = "[\"USER\"]")
    private Set<String> role = Set.of("USER");

    public SignupRequestBody(String email, String password, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
