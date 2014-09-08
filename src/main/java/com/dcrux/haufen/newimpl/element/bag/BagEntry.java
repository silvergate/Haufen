package com.dcrux.haufen.newimpl.element.bag;

import com.dcrux.haufen.element.bag.IBagElement;
import com.dcrux.haufen.element.bag.IBagEntry;
import com.dcrux.haufen.newimpl.IInternalElement;

/**
 * Created by caelis on 02/09/14.
 */
public class BagEntry implements IBagEntry {
    private final IInternalElement element;
    private final int count;

    public BagEntry(IInternalElement element, int count) {
        this.element = element;
        this.count = count;
    }

    @Override
    public IInternalElement getElement() {
        return element;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "BagEntry{" +
                "element=" + element +
                ", count=" + count +
                '}';
    }
}
