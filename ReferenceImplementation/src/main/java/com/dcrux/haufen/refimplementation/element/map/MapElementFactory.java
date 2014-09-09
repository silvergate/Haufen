package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.Type;
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
    public IInternalElement createUninitialized() {
        return new MapElement(false);
    }

    @Override
    public IInternalElement create() {
        return new MapElement(true);
    }
}
