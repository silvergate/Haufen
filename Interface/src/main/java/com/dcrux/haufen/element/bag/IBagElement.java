package com.dcrux.haufen.element.bag;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.element.common.ICollection;

/**
 * Created by caelis on 08/09/14.
 */

public interface IBagElement extends IElement, ICollection<IBagEntry, IBagElement> {

    // TODO: Implement ITypedAccessor
    int addCount(IElement element);

    IBagElement add(IElement element);
}
