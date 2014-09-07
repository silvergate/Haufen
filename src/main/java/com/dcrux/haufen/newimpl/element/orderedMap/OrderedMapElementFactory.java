package com.dcrux.haufen.newimpl.element.orderedMap;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.orderedMap;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new OrderedMapElement(data, subtype, elementProvider);
    }

    @Override
    public IInternalElement create() {
        return new OrderedMapElement();
    }
}
