package com.dcrux.haufen.newimpl.element.bag;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class BagElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.bag;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new BagElement(data, subtype, elementProvider);
    }

    @Override
    public IInternalElement create() {
        return new BagElement();
    }
}