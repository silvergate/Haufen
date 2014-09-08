package com.dcrux.haufen.element.integer;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 07/09/14.
 */
public interface IIntegerElement extends IElement {
    IntegerType getIntegerType();

    IIntegerElement setIntegerType(IntegerType integerType);

    IIntegerElement set(long value);

    IIntegerElement uset(long value);

    long get();
}
