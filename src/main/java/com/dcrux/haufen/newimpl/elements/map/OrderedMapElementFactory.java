package com.dcrux.haufen.newimpl.elements.map;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.orderedMap;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new OrderedMapElement(data, subtype, elementProvider);
    }

    @Override
    public IElement create() {
        return new OrderedMapElement();
    }
}
