package com.dcrux.haufen.impl.types.string;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

/**
 * Created by caelis on 26/07/14.
 */
//TODO: 2 Versionen (subtype) machen. Eine wenn bytes<X ist (dann kein char-length includen), wenn >X, dann mit char length prefixnen. X=?. Würde mal sagen etwa 8, 10, 12 oder 16...
public class StringType implements IStreamType {

    @Override
    public BaseType getBaseType() {
        return BaseType.string;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        final byte[] asByte = ((String) payload).getBytes("UTF-8");
        dataOutput.write(asByte);
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        byte[] strData = new byte[(int) length];
        dataInput.readFully(strData);
        return new String(strData, "UTF-8");
    }
}
