package com.dcrux.haufen.element.common;

import com.dcrux.haufen.IElement;

import java.util.Iterator;

/**
 * Created by caelis on 07/09/14.
 */
public interface ICollection<TElement> extends IElement {
    //TODO: Split into a readonly and writable version?

    ICollection clear();

    Iterator<TElement> iterator();

    boolean isEmpty();

    //TODO: int getSize() ?
}
