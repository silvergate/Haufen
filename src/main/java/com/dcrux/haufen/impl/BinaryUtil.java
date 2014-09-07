package com.dcrux.haufen.impl;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;

import java.util.Comparator;

/**
 * Created by caelis on 22/07/14.
 */
public class BinaryUtil {

    public static void readHead(IDataInput dataInput, Head outHead) {
        //bbbbbssa
        byte unmasked = dataInput.readByte();
        int value = unmasked & 255;
        byte baseType = (byte) (value >>> 3);
        byte subtype = (byte) ((value & 0b00000111) >>> 1);
        byte annotation = (byte) (value & 0b00000001);

        outHead.setBaseType(BaseType.fromCode(baseType));
        outHead.setSubtype(subtype);
        outHead.setFollowingAnnotation(annotation == 1);
    }

    public static void writeHead(IDataOutput dataOutput, Head inHead) {
        //bbbbbssa
        byte baseType = inHead.getBaseType().getCode();
        byte subtype = inHead.getSubtype();
        byte annotation = inHead.isFollowingAnnotation() ? (byte) 1 : 0;

        int value = 0;
        value = value | (annotation);
        value = value | (subtype << 1);
        value = value | (baseType << 3);

        dataOutput.writeByte((byte) value);
    }

    public static void reverse(byte[] b) {
        for (int i = 0; i < b.length / 2; i++) {
            byte x1 = b[i];
            byte x2 = b[b.length - 1 - i];
            b[i] = x2;
            b[b.length - 1 - i] = x1;
        }
    }

    public static byte[] reverseCopy(byte[] b) {
        byte[] reversed = new byte[b.length];
        System.arraycopy(b, 0, reversed, 0, b.length);
        reverse(reversed);
        return reversed;
    }

    public static Comparator<byte[]> getBinaryComparator() {
        return (o1, o2) -> {
            if (o1.length != o2.length)
                return o2.length - o1.length;
            for (int i = 0; i < o1.length; i++) {
                int dif = o2[i] - o1[i];
                if (dif != 0)
                    return dif;
            }
            return 0;
        };
    }


}
