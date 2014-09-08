package com.dcrux.haufen.newimpl.element.annotation;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 01/09/14.
 */
public class AnnotationElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.annotation;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        return new AnnotationElement(data, subtype, elementProvider);
    }

    @Override
    public IInternalElement create() {
        return new AnnotationElement();
    }
}
