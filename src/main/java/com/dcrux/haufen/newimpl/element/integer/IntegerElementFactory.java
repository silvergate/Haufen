package com.dcrux.haufen.newimpl.element.integer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.utils.Varint;

/**
 * Created by caelis on 01/09/14.
 */
public class IntegerElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.integer;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        IntegerElement integerElement = new IntegerElement(0, subtype);
        long value = 0;
        switch (integerElement.getIntegerType()) {
            case signed:
                value = Varint.readSignedVarLong(data);
                break;
            case unsigned:
                value = Varint.readUnsignedVarLong(data);
                break;
        }
        integerElement.set(value);
        return integerElement;
    }

    @Override
    public IInternalElement create() {
        return new IntegerElement();
    }
}
