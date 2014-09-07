package com.dcrux.haufen.newimpl.element.string;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 28/08/14.
 */
public class StringElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.string;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new StringElement(data);
    }

    @Override
    public IInternalElement create() {
        return new StringElement("");
    }
}
