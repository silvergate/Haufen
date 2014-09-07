package com.dcrux.haufen.newimpl.element.bag;

import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 02/09/14.
 */
public class BagEntry {
    private final IInternalElement element;
    private final int count;

    public BagEntry(IInternalElement element, int count) {
        this.element = element;
        this.count = count;
    }

    public IInternalElement getElement() {
        return element;
    }

    public int getCount() {
        return count;
    }
}
