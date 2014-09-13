package com.dcrux.haufen.refimplementation.element.empty;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class EmptyElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.empty;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return EmptyElement.getSingleton();
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return EmptyElement.getSingleton();
    }
}
