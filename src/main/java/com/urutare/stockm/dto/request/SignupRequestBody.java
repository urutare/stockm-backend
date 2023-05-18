package com.urutare.stockm.dto.request;

import java.util.Set;

import lombok.Data;

@Data
public class SignupRequestBody {
    String email;
    String password;
    String fullName;
    String phoneNumber;
    private Set<String> role;
    private String username;

    public SignupRequestBody(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

}
