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
        return (T) as(type.getType(), IElement.class);
    }

    @Override
    public <T extends IElement> T as(Type type, Class<T> clazz) {
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

        if (type != thisElement.getType())
            throw new IllegalArgumentException("Element is of wrong type");
        return (T) thisElement;
    }

    @Override
    public IElement that() {
        if (getType() == Type.annotated) {
            return as(Types.ANNOTATED).get();
        }
        return this;
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
