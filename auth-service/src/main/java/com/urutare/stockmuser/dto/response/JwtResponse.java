package com.urutare.stockmuser.dto.response;

import com.urutare.stockmuser.service.UserDetailsImpl;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class JwtResponse {
    private UUID id;
    private String name;
    private String emailOrPhone;
    private String image;
    private List<String> roles;


    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresAt;
    private long refreshTokenExpiresAt;


    public JwtResponse(UserDetailsImpl userDetails, String accessToken, String refreshToken) {
        this.id = userDetails.getId();
        this.emailOrPhone = userDetails.getUsername();
        this.roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        String firstName = userDetails.getFirstName() != null ? userDetails.getFirstName() : "";
        String lastName = userDetails.getLastName() != null ? userDetails.getLastName() : "";

        this.name = firstName + " " + lastName;
        this.image = userDetails.getAvatar();
    }
}
