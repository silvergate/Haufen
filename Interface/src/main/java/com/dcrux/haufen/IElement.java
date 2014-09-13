package com.dcrux.haufen;


/**
 * Created by caelis on 19/08/14.
 */
public interface IElement extends IEqualsHashcode {
    long getSizeFootprint();

    Type getType();

    <T extends IElement> T as(Types<T> type);

    <T extends IElement> T as(Type type, Class<T> clazz);

    IElement that();

    boolean is(Type type);
}
