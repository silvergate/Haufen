package com.dcrux.haufen.newimpl.element.orderedMap;

import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.element.map.MapElement;

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
