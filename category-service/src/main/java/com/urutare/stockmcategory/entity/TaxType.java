package com.urutare.stockmcategory.entity;

import lombok.Getter;

@Getter
public enum TaxType {
    A("A-EX"), B("B-18.00%"), C("C"), D("D");

    private final String taxType;

    TaxType(String taxType) {
        this.taxType = taxType;
    }

    @Override
    public String toString() {
        return taxType;
    }
}
