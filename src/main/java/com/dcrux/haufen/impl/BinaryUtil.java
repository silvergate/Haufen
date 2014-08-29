package com.dcrux.haufen.impl;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.common.Type;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by caelis on 22/07/14.
 */
public class BinaryUtil {
    private static void writeTypeCode(IDataOutput dataOutput, Type type) throws IOException {
        Varint.writeUnsignedVarInt(getTypeCode(type), dataOutput);
    }

    private static int getTypeCode(Type type) {
        return getTypeCode(type.getBaseType(), type.getSubtype());
    }

    private static int getTypeCode(BaseType baseType, byte subtype) {
        return (baseType.getCode() + (subtype * 32));
    }

    @Nullable
    private static Type readTypeCode(IDataInput dataInput) throws IOException {
        int value = Varint.readUnsignedVarInt(dataInput);
        byte baseType = (byte) (value % 32);
        byte subtype = (byte) ((value - baseType) / 32);
        return new Type(BaseType.fromCode(baseType), subtype);
    }

    public static void readHead(IDataInput dataInput, Head outHead) throws IOException {
        //bbbbbssa
        byte unmasked = dataInput.readByte();
        int value = unmasked & 255;
        byte baseType = (byte)(value >>> 3);
        byte subtype = (byte)(value & 0b00000111 >>> 1);
        byte annotation = (byte)(value & 0b00000001);

        outHead.setBaseType(BaseType.fromCode(baseType));
        outHead.setSubtype(subtype);
        outHead.setFollowingAnnotation(annotation==1);
    }

    public static void writeHead(IDataOutput dataOutput, Head inHead) throws IOException {
        //bbbbbssa
        byte baseType = inHead.getBaseType().getCode();
        byte subtype = inHead.getSubtype();
        byte annotation = inHead.isFollowingAnnotation() ? (byte)1 : 0;

        int value = 0;
        value = value | (annotation);
        value = value | (subtype << 1);
        value = value | (baseType << 3);

        dataOutput.writeByte((byte)value);
    }

    public static void reverse(byte[] b) {
        for (int i = 0; i < b.length / 2; i++) {
            byte x1 = b[i];
            byte x2 = b[b.length - 1 - i];
            b[i] = x2;
            b[b.length - 1 - i] = x1;
        }
    }

    public static Comparator<byte[]> getBinaryComparator() {
        return (o1, o2) -> {
            if (o1.length!=o2.length)
                return o2.length - o1.length;
            for (int i=0; i<o1.length;i++) {
                int dif = o2[i] - o1[i];
                if (dif!=0)
                    return dif;
            }
            return 0;
        };
    }

    public static void transfer(IDataOutput out, IDataInput in) {
        //TODO
    }

}
