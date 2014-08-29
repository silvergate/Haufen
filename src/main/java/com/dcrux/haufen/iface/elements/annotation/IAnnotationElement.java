package com.dcrux.haufen.iface.elements.annotation;

import com.dcrux.haufen.iface.elements.IElement;

/**
 * Created by caelis on 19/08/14.
 */
public interface IAnnotationElement extends IElement {
    byte[] getData();
    void setData(byte[] data);
}
