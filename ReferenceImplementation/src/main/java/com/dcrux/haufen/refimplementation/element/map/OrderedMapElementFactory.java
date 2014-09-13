package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.orderedMap;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return new OrderedMapElement(false, elementCreator);
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return new OrderedMapElement(true, elementCreator);
    }
}
