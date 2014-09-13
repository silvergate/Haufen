package com.dcrux.haufen.element.common;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 13/09/14.
 */
public interface IElementPair<TFirst extends IElement, TSecond extends IElement> {
    TFirst getFirst();

    TSecond getSecond();
}
