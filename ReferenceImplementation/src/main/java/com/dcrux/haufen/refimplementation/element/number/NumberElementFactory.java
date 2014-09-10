package com.dcrux.haufen.refimplementation.element.number;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class NumberElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.number;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new NumberElement();
    }

    @Override
    public IInternalElement create() {
        return new NumberElement();
    }
}
