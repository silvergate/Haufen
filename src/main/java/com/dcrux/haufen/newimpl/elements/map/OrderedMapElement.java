package com.dcrux.haufen.newimpl.elements.map;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.newimpl.IElementProvider;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElement extends MapElement {
    public OrderedMapElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        super(dataInput, subtype, elementProvider);
    }

    public OrderedMapElement() {
        super();
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }
}
