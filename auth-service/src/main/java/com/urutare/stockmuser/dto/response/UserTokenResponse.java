package com.urutare.stockmuser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenResponse {
    private UUID userId;
    private String username;
    private ArrayList<?> roles;
}
