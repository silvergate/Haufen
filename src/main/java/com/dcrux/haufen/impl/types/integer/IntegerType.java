package com.dcrux.haufen.impl.types.integer;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

/**
 * Created by caelis on 12/08/14.
 */
public class IntegerType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.integer;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        if (subtype == 0) {
            Varint.writeSignedVarLong((long) payload, dataOutput);
        }
        //TODO: Other types
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        if (subtype == 0) {
            return Varint.readUnsignedVarLong(dataInput);
        }
        throw new IllegalArgumentException();
    }
}
