package com.urutare.stockmcategory.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Mass {
    KG("kg"),
    HG("hg"),
    DAG("dag"),
    G("g");

    private final String name;

    Mass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> valuesString() {
        List<String> list = new ArrayList<>();
        for (Mass mass : values()) {
            list.add(mass.getName());
        }
        return list;
    }

    public static String getClassName() {
        return Mass.class.getSimpleName();
    }

    public String getClassN() {
        return getClassName();
    }
}
