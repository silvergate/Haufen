package com.dcrux.haufen.impl.common;

import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 22/07/14.
 */
public enum BaseType {
    integer((byte) 0),
    number((byte) 1),
    string((byte) 2),
    bool((byte) 3),
    binary((byte) 4),
    map((byte) 5),
    table((byte) 6),
    bag((byte) 7),
    set((byte) 8),
    array((byte) 9),
    annotation((byte) 10),
    index((byte) 31);

    private byte code;

    BaseType(byte code) {
        if (code < 0 || code > 31)
            throw new IllegalArgumentException("");
        this.code = code;
    }

    @Nullable
    public static BaseType fromCode(byte code) {
        //TODO: Switch-case for speed
        for (BaseType baseType : BaseType.values()) {
            if (baseType.getCode() == code)
                return baseType;
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}
