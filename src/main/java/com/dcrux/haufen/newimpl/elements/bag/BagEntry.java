package com.dcrux.haufen.newimpl.elements.bag;

import com.dcrux.haufen.newimpl.IElement;

/**
 * Created by caelis on 02/09/14.
 */
public class BagEntry {
    private final IElement element;
    private final int count;

    public BagEntry(IElement element, int count) {
        this.element = element;
        this.count = count;
    }

    public IElement getElement() {
        return element;
    }

    public int getCount() {
        return count;
    }
}
