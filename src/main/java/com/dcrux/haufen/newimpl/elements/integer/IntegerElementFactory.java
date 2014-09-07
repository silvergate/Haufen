package com.dcrux.haufen.newimpl.elements.integer;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 01/09/14.
 */
public class IntegerElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.integer;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        IntegerElement integerElement = new IntegerElement(0, subtype);
        long value = 0;
        switch (integerElement.getType()) {
            case signed:
                value = Varint.readSignedVarLong(data);
                break;
            case unsigned:
                value = Varint.readUnsignedVarLong(data);
                break;
        }
        integerElement.setValue(value);
        return integerElement;
    }

    @Override
    public IElement create() {
        return new IntegerElement();
    }
}
