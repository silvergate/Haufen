package com.dcrux.haufen;

import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.element.annotation.IAnnotationElement;
import com.dcrux.haufen.element.bag.IBagElement;
import com.dcrux.haufen.element.bool.IBoolElement;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.string.IStringElement;

/**
 * Created by caelis on 08/09/14.
 */
public final class Types<T extends IElement> {

    public static final Types<IIntegerElement> INTEGER = new Types<>(Type.integer);
    public static final Types<IAnnotatedElement> ANNOTATED = new Types<>(Type.annotated);
    public static final Types<IAnnotationElement> ANNOTATION = new Types<>(Type.annotation);
    public static final Types<IStringElement> STRING = new Types<>(Type.string);
    public static final Types<IBagElement> BAG = new Types<>(Type.bag);
    public static final Types<IMapElement> MAP = new Types<>(Type.map);
    public static final Types<IBoolElement> BOOL = new Types<>(Type.bool);

    private final Type type;

    public Types(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
