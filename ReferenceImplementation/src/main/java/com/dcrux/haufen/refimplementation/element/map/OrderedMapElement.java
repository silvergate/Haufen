package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.element.map.IOrderedMapElement;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElement extends MapElement implements IOrderedMapElement {

    public OrderedMapElement(boolean initialized) {
        super(initialized);
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }
}
