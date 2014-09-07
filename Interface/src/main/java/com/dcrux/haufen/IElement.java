package com.dcrux.haufen;


/**
 * Created by caelis on 19/08/14.
 */
public interface IElement extends IEqualsHashcode {
    long getSizeFootprint();

    Type getType();

    <T extends IElement> T as(Types<T> type);

    boolean is(Type type);
}
