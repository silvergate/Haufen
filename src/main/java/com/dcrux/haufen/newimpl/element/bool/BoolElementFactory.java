package com.dcrux.haufen.newimpl.element.bool;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class BoolElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.bool;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new BoolElement(data);
    }

    @Override
    public IInternalElement create() {
        return new BoolElement();
    }
}
