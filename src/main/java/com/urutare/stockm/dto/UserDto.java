package com.urutare.stockm.dto;

import com.urutare.stockm.entity.User;

// User Data Transfer Object
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
}
