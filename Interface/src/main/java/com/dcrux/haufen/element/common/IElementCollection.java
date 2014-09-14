package com.dcrux.haufen.element.common;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 07/09/14.
 */
public interface IElementCollection<TEntry extends IElement, TThis extends IElementCollection<TEntry, TThis>>
        extends ICollection<TEntry, TThis> {

}
