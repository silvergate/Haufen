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

    // (keys) ordered: false, duplicates: false
    map((byte) 5),
    // (keys) ordered: true, duplicates: false
    orderedMap((byte) 6),

    // ordered: false, duplicates: true
    bag((byte) 8),
    // ordered: true, duplicates: true
    orderedBag((byte) 9), //TODO: MISSING

    // ordered: false, duplicates: false
    set((byte) 10),
    // ordered: true, duplicates: false
    orderedSet((byte) 11),

    // ordered: true, duplicates: true
    array((byte) 12),

    table((byte) 14), //TODO: MISSING

    annotation((byte) 13), //TODO: MISSING
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
