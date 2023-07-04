package com.urutare.stockmcategory.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Liquid {
    L("l"),
    M3("m3"),
    DM3("dm3"),
    KL("kl"),
    ML("mL");

    private final String name;

    Liquid(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> valuesString() {
        List<String> list = new ArrayList<>();
        for (Liquid liquid : values()) {
            list.add(liquid.getName());
        }
        return list;
    }

    public static String getClassName() {
        return Liquid.class.getSimpleName();
    }

    public String getClassN() {
        return getClassName();
    }
}
