package com.dcrux.haufen.impl.types.annotation;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

/**
 * Created by caelis on 18/08/14.
 */
public class AnnotationType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.annotation;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        dataOutput.write((byte[])payload);
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        byte[] annotation = new byte[(int) length];
        dataInput.readFully(annotation, 0, (int) length);
        return annotation;
    }
}
