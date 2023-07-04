package com.urutare.stockmcategory.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Length {
    KM("km"),
    HM("hm"),
    DAM("dam"),
    M("m");

    private final String name;

    Length(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> valuesString() {
        List<String> list = new ArrayList<>();
        for (Length length : values()) {
            list.add(length.getName());
        }
        return list;
    }

    public static String getClassName() {
        return Length.class.getSimpleName();
    }

    public String getClassN() {
        return getClassName();
    }
}
