package com.urutare.stockmcategory.models.enums;

public enum Unit {
    KG(Mass.class, UnitClass.MASS),
    L(Liquid.class, UnitClass.LIQUID),
    M(Length.class, UnitClass.LENGTH),
    PIECE(null, UnitClass.PIECE),
    SERVICE(null, UnitClass.SERVICE),
    OTHER(null, UnitClass.OTHER);

    private final Class<? extends Enum<?>> enumClass;
    private final UnitClass className;

    Unit(Class<? extends Enum<?>> enumClass, UnitClass className) {
        this.enumClass = enumClass;
        this.className = className;
    }

    public String getName() {
        return enumClass.getSimpleName();
    }

    public UnitClass getClassName() {
        return className;
    }

    public enum UnitClass {
        MASS,
        LIQUID,
        LENGTH,
        PIECE,
        SERVICE,
        OTHER,
    }
}
