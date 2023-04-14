package com.urutare.stockm.models;

public enum Role {
    admin("Admin"), seller("Seller"), buyer("Buyer");
    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
