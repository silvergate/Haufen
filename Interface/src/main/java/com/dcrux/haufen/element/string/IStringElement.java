package com.dcrux.haufen.element.string;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 07/09/14.
 */
public interface IStringElement extends IElement {
    IStringElement set(String value);

    String get();
}
