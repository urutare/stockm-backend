package com.urutare.stockmuser.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class JwtResponse {
  private UUID id;
  private String email;
  private List<String> roles;

  private String type = "Bearer";
  private String accessToken;
  private String refreshToken;

  public JwtResponse(String accessToken, String refreshToken, UUID id, String email,
      List<String> roles) {
    this.id = id;
    this.email = email;
    this.roles = roles;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
