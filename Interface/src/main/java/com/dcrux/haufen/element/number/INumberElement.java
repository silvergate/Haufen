package com.dcrux.haufen.element.number;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 07/09/14.
 */
public interface INumberElement extends IElement {
    NumberType getNumberType();

    INumberElement setNumberType(NumberType numberType);

    INumberElement set(long mantissa, long exponent);

    INumberElement setDecimal(long mantissa, long exponent);

    long getMantissa();

    long getExponent();
}
