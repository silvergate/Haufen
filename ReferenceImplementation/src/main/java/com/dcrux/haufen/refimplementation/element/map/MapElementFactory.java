package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class MapElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.map;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return new MapElement(false, elementCreator);
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return new MapElement(true, elementCreator);
    }
}
