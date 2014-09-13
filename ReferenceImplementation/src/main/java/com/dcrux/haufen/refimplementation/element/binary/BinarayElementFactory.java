package com.dcrux.haufen.refimplementation.element.binary;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class BinarayElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.binary;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return new BinaryElement(false);
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return new BinaryElement(true);
    }
}
