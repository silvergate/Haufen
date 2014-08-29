package com.dcrux.haufen.impl.types.bool;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

/**
 * Created by caelis on 17/08/14.
 */
public class BooleanType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.bool;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        Boolean value = (Boolean) payload;
        if (value) {
            Varint.writeUnsignedVarInt(1, dataOutput);
        } else {
            Varint.writeUnsignedVarInt(0, dataOutput);
        }
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        final int value = Varint.readUnsignedVarInt(dataInput);
        switch (value) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Got value: " + value);
        }
    }
}
