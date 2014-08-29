package com.dcrux.haufen.iface;

import com.dcrux.haufen.iface.elements.annotation.IAnnotationElement;

/**
 * Created by caelis on 19/08/14.
 */
public interface IHaufen {
    IAnnotationElement annotation(byte[] data);
}
