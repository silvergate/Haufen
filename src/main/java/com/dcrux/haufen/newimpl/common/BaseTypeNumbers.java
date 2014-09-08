package com.dcrux.haufen.newimpl.common;

import com.dcrux.haufen.Type;

/**
 * Created by caelis on 07/09/14.
 */
public class BaseTypeNumbers {
    public static Type fromNumber(byte number) {
        switch (number) {
            case 0:
                return Type.integer;
            case 1:
                return Type.number;
            case 2:
                return Type.string;
            case 3:
                return Type.bool;
            case 4:
                return Type.binary;
            case 5:
                return Type.map;
            case 6:
                return Type.orderedMap;
            case 8:
                return Type.bag;
            case 9:
                return Type.orderedBag;
            case 10:
                return Type.set;
            case 11:
                return Type.orderedSet;
            case 12:
                return Type.array;
            case 14:
                return Type.table;
            case 13:
                return Type.annotation;
            case 15:
                return Type.annotated;
            case 31:
                return Type.index;
            default:
                throw new IllegalArgumentException("Unknown code: " + number);
        }
    }

    public static byte toNumber(Type type) {
        switch (type) {
            case integer:
                return 0;
            case number:
                return 1;
            case string:
                return 2;
            case bool:
                return 3;
            case binary:
                return 4;
            case map:
                return 5;
            case orderedMap:
                return 6;
            case bag:
                return 8;
            case orderedBag:
                return 9;
            case set:
                return 10;
            case orderedSet:
                return 11;
            case array:
                return 12;
            case table:
                return 14;
            case annotation:
                return 13;
            case annotated:
                return 15;
            case index:
                return 31;
            default:
                throw new IllegalArgumentException("Unknown enum: " + type);
        }
    }
}
