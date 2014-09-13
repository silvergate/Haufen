package com.dcrux.haufen.refimplementation.element.set;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class SetElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.set;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return new SetElement(false);
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return new SetElement(true);
    }
}
