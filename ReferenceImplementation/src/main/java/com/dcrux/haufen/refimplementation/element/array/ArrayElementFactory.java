package com.dcrux.haufen.refimplementation.element.array;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class ArrayElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.list;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new ListElement(false);
    }

    @Override
    public IInternalElement create() {
        return new ListElement(true);
    }
}
