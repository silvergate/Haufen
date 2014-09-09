package com.dcrux.haufen.refimplementation.element.annotation;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class AnnotationElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.annotation;
    }

    @Override
    public IInternalElement createUninitialized() {
        return new AnnotationElement(false);
    }

    @Override
    public IInternalElement create() {
        return new AnnotationElement(true);
    }
}
