package com.urutare.stockmuser.utils;

import java.util.Map;

public enum JsonUtils {
    instance;

    public static JsonUtils of() {
        return instance;
    }

    public String toJson(Map<String, String> data) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        json = new StringBuilder(json.substring(0, json.length() - 1));
        json.append("}");
        return json.toString();
    }
}
