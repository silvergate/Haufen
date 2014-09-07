package com.dcrux.haufen.newimpl.elements.bag;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 01/09/14.
 */
public class BagElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.bag;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new BagElement(data, subtype, elementProvider);
    }

    @Override
    public IElement create() {
        return new BagElement();
    }
}
