package com.dcrux.haufen.impl.common;

/**
 * Created by caelis on 24/07/14.
 */
public class Type {

    private final BaseType baseType;
    private final byte subtype;

    public Type(BaseType baseType, byte subtype) {
        this.baseType = baseType;
        if (subtype > 3 || subtype < 0)
            throw new IllegalArgumentException("");
        this.subtype = subtype;
    }

    public static Type bool() {
        return new Type(BaseType.bool, (byte) 0);
    }

    public static Type integer() {
        return new Type(BaseType.integer, (byte) 0);
    }

    public static Type string() {
        return new Type(BaseType.string, (byte) 0);
    }

    public static Type map() {
        return new Type(BaseType.map, (byte) 0);
    }

    public static Type bag() {
        return new Type(BaseType.bag, (byte) 0);
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public byte getSubtype() {
        return subtype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (subtype != type.subtype) return false;
        if (baseType != type.baseType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = baseType.hashCode();
        result = 31 * result + (int) subtype;
        return result;
    }

    @Override
    public String toString() {
        return "Type{" +
                "baseType=" + baseType +
                ", subtype=" + subtype +
                '}';
    }
}
