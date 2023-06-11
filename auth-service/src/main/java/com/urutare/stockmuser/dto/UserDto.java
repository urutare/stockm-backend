package com.urutare.stockmuser.dto;

import java.util.UUID;

import com.urutare.stockmuser.entity.User;

import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private String fullName;
    
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }
    
}
