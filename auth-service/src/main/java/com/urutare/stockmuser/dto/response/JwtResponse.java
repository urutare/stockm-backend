package com.urutare.stockmuser.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JwtResponse {
    private UUID id;
    private String email;
    private List<String> roles;

    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresAt;
    private long refreshTokenExpiresAt;

    public JwtResponse(String accessToken, String refreshToken, UUID id, String email,
                       List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
