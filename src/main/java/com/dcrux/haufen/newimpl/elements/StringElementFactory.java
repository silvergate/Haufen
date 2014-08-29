package com.dcrux.haufen.newimpl.elements;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 28/08/14.
 */
public class StringElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.string;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new StringElement(data);
    }

    @Override
    public IElement create() {
        return new StringElement("");
    }
}
