package com.dcrux.haufen.element.list;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.element.common.IAccessor;
import com.dcrux.haufen.element.common.ICollection;

/**
 * Created by caelis on 08/09/14.
 */
public interface IListElement extends IElement, ICollection<IElement, IListElement>,
        IAccessor<IElement> {
    IListElement insert(int index, IElement element);

    IListElement removeAll(IElement element);

    IElement getAccessor(int index);
}
