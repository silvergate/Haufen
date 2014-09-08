package com.dcrux.haufen.element.annotation;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 08/09/14.
 */
public interface IAnnotationElement extends IElement {
    IAnnotationElement set(byte[] data);

    byte[] get();
}
