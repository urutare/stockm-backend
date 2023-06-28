package com.urutare.stockmcategory.entity;

import lombok.Getter;

public enum TaxType {
    A("A-EX"), B("B-18.00%"), C("C"), D("D");

    @Getter
    private final String taxType;

    TaxType(String taxType) {
        this.taxType = taxType;
    }

    @Override
    public String toString() {
        return taxType;
    }
}
