package com.urutare.stockmcategory.entity;

import lombok.Getter;

public enum MeasurementType {
    MASS("Mass"),
    LENGTH("Length"),
    VOLUME("Volume"),
    SERVICE("Service-Unit"),
    PIECE("Piece-Unit");

    @Getter
    private final String measurementType;

    MeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    @Override
    public String toString() {
        return measurementType;
    }
}
