package com.dcrux.haufen.refimplementation.element.list;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class ListElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.list;
    }

    @Override
    public IInternalElement createUninitialized(IElementCreator elementCreator) {
        return new ListElement(false, elementCreator);
    }

    @Override
    public IInternalElement create(IElementCreator elementCreator) {
        return new ListElement(true, elementCreator);
    }
}
