package com.urutare.stockm.utils;

import com.urutare.stockm.entity.User;

import java.util.HashMap;
import java.util.Map;

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
            map.put("isActive", user.isActive());
            map.put("avatar", user.getAvatar());
            map.put("verified", user.isVerified());
            map.put("lastTimePasswordUpdated", user.getLastTimePasswordUpdated());
            map.put("birthdate", user.getBirthdate());
            map.put("lastLogin", user.getLastLogin());
        }
        return map;
    }

    public User fromMap(Map<String, Object> map, Class<User> userClass) {
        User user = new User();
        user.setId(Long.parseLong(map.get("id").toString()));
        user.setEmail((String) map.get("email"));
        user.setFullName((String) map.get("fullName"));
        user.setPhoneNumber((String) map.get("phoneNumber"));
        return user;
    }
}
