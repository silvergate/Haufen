package com.dcrux.haufen.refimplementation.element.string;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 28/08/14.
 */
public class StringElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.string;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new StringElement();
    }

    @Override
    public IInternalElement create() {
        return new StringElement("");
    }
}
