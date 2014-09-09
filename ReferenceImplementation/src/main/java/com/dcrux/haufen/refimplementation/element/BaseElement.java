package com.dcrux.haufen.refimplementation.element;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 08/09/14.
 */
public abstract class BaseElement implements IInternalElement, IElement {

    @Override
    public <T extends IElement> T as(Types<T> type) {
        final Type actualType = getType();
        final IElement thisElement;
        /* If annotated, take the targed (except if we request the annotation) */
        if (actualType == Type.annotated && type.getType() != Type.annotated) {
            if (!(this instanceof IAnnotatedElement)) {
                throw new IllegalArgumentException("This element is of wrong type (should not happen).");
            }
            thisElement = ((IAnnotatedElement) this).get();
        } else {
            thisElement = this;
        }

        if (type.getType() != thisElement.getType())
            throw new IllegalArgumentException("Element is of wrong type");
        return (T) thisElement;
    }

    @Override
    public boolean is(Type type) {
        final Type actualType = getType();
        final IElement thisElement;
        /* If annotated, take the targed (except if we request the annotation) */
        if (actualType == Type.annotated && type != Type.annotated) {
            if (!(this instanceof IAnnotatedElement)) {
                throw new IllegalArgumentException("This element is of wrong type (should not happen).");
            }
            thisElement = ((IAnnotatedElement) this).get();
        } else {
            thisElement = this;
        }
        return thisElement.getType() == type;
    }
}
