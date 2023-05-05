package com.urutare.stockm.dto.request;

import lombok.Data;

@Data
public class SignupRequestBody {
    String email;
    String password;
    String fullName;
    String phoneNumber;

    public SignupRequestBody(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

}
