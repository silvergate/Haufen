package com.dcrux.haufen;

import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.element.integer.IIntegerElement;

/**
 * Created by caelis on 08/09/14.
 */
public final class Types<T extends IElement> {

    public static final Types<IIntegerElement> INTEGER = new Types<>(Type.integer);
    public static final Types<IAnnotatedElement> ANNOTATED = new Types<>(Type.annotated);

    private final Type type;

    public Types(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
