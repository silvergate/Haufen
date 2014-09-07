package com.dcrux.haufen.newimpl.element.set;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class SetElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.set;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new SetElement(data, subtype, elementProvider);
    }

    @Override
    public IInternalElement create() {
        return new SetElement();
    }
}
