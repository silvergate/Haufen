package com.dcrux.haufen.refimplementation.element.bag;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class BagElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.bag;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new BagElement(false);
    }

    @Override
    public IInternalElement create() {
        return new BagElement(true);
    }
}
