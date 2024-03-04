package com.urutare.stockmuser.utils;

import com.urutare.stockmuser.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum MapUtils {
    instance;

    public static MapUtils of() {
        return instance;
    }

    public Map<String, Object> toMap(User user, boolean simplified) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("email", user.getEmail());
        map.put("fullName", user.getFullName());
        if (!simplified) {
            map.put("phoneNumber", user.getPhoneNumber());
            map.put("roles", user.getRoles().toString());
            map.put("avatar", user.getAvatar());
            map.put("isEmailVerified", user.isEmailVerified());
            map.put("isPhoneVerified", user.isPhoneVerified());
        }
        return map;
    }

    public User fromMap(Map<String, Object> map, Class<User> userClass) {
        User user = new User();
        user.setId((UUID) map.get("id"));
        user.setEmail((String) map.get("email"));
        user.setFullName((String) map.get("fullName"));
        user.setPhoneNumber((String) map.get("phoneNumber"));
        return user;
    }
}
