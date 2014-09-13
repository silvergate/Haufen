package com.dcrux.haufen.element.map;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.element.common.IElementPair;

/**
 * Created by caelis on 08/09/14.
 */
public interface IOrderedMapElement extends IBaseMapElement {
    IElementPair<IElement, IElement> getAccessorForValue(int index);

    IElementPair<IElement, IElement> getAccessorForKey(int index);
}
