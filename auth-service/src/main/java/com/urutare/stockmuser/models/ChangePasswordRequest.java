package com.urutare.stockmuser.models;

public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
