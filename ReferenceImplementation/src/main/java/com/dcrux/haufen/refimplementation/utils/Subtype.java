package com.dcrux.haufen.refimplementation.utils;

/**
 * Created by caelis on 01/09/14.
 */
public class Subtype {
    public static boolean isFlag0(byte subtype) {
        return (subtype & 0b1) == 0b1;
    }

    public static boolean isFlag1(byte subtype) {
        return (subtype & 0b10) == 0b10;
    }

    public static boolean isFlag2(byte subtype) {
        return (subtype & 0b100) == 0b100;
    }

    public static byte setFlag0(byte subtype) {
        return (byte) (subtype | 0b1);
    }

    public static byte setFlag1(byte subtype) {
        return (byte) (subtype | 0b10);
    }

    public static byte setFlag2(byte subtype) {
        return (byte) (subtype | 0b100);
    }

    public static byte unsetFlag0(byte subtype) {
        return (byte) (subtype ^ 0b1);
    }

    public static byte unsetFlag1(byte subtype) {
        return (byte) (subtype ^ 0b10);
    }

    public static byte unsetFlag2(byte subtype) {
        return (byte) (subtype ^ 0b100);
    }
}
