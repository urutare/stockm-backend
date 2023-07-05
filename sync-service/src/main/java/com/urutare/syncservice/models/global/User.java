package com.urutare.syncservice.models.global;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class User {
    private UUID id;

    private String email;

    private String password;

    private String username;

    private String fullName;

    private String phoneNumber;

    private Set<Role> roles = new HashSet<>();

    private boolean isEnabled;

    private boolean isActive;

    private String avatar;

    private boolean verified;

}
