package com.dcrux.haufen.refimplementation.element.integer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class IntegerElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.integer;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new IntegerElement();
    }

    @Override
    public IInternalElement create() {
        return new IntegerElement();
    }
}
