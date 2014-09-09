package com.dcrux.haufen.refimplementation.element.bool;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class BoolElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.bool;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new BoolElement(false);
    }

    @Override
    public IInternalElement create() {
        return new BoolElement(true);
    }
}
