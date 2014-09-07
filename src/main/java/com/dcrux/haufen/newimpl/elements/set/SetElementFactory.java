package com.dcrux.haufen.newimpl.elements.set;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 01/09/14.
 */
public class SetElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.set;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new SetElement(data, subtype, elementProvider);
    }

    @Override
    public IElement create() {
        return new SetElement();
    }
}
