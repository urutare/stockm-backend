package com.urutare.stockm.models;

public enum ERole {
    admin("Admin"), seller("Seller"), buyer("Buyer");
    private final String displayName;

    ERole(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
