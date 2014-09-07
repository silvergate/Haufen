package com.dcrux.haufen.newimpl.utils;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;

/**
 * Created by caelis on 31/08/14.
 */
//TOD: Rename to DataInputUtil
public class DataCopyUtil {
    public static DataCopyUtil instance() {
        return new DataCopyUtil();
    }

    public void copy(IDataInput input, IDataOutput output) {
        //TODO: Improve
        for (long i = 0; i < input.getLength(); i++) {
            output.write(new byte[]{input.readByte()});
        }
    }

    public boolean equals(IDataInput o1, IDataInput o2) {
        o1.seek(0);
        o2.seek(0);
        long dif = o1.getLength() - o2.getLength();
        if (dif != 0)
            return false;
        for (long i = 0; i < o1.getLength(); i++) {
            byte b1 = o1.readByte();
            byte b2 = o2.readByte();
            int valDif = Integer.compare(Byte.toUnsignedInt(b1), Byte.toUnsignedInt(b2));
            if (valDif != 0)
                return false;
        }
        return true;
    }

    public int hashCode(IDataInput dataInput) {
        dataInput.seek(0);
        int result = (int) (dataInput.getLength() % Short.MAX_VALUE);
        /* Take max 20 bytes */
        int len = dataInput.getLength() < 20 ? (int) dataInput.getLength() : 20;
        for (long i = 0; i < len; i++) {
            result = 31 * result + dataInput.readByte();
        }
        return result;
    }

    public int canonicalCompare(IDataInput o1, IDataInput o2) {
        o1.seek(0);
        o2.seek(0);
        long dif = o1.getLength() - o2.getLength();
        if (dif != 0) {
            if (dif > Integer.MAX_VALUE)
                return Integer.MAX_VALUE;
            if (dif < Integer.MIN_VALUE)
                return Integer.MIN_VALUE;
            return (int) dif;
        }
        for (long i = 0; i < o1.getLength(); i++) {
            byte b1 = o1.readByte();
            byte b2 = o2.readByte();
            int valDif = Integer.compare(Byte.toUnsignedInt(b1), Byte.toUnsignedInt(b2));
            if (valDif != 0)
                return valDif;
        }
        return 0;
    }
}
