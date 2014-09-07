package com.dcrux.haufen.newimpl;

import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;

import java.util.Iterator;

/**
 * Created by caelis on 27/08/14.
 */
public interface IElement extends IEqualsHashcode, AutoCloseable {
    BaseType getBaseType();

    byte getSubtype();

    void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider);

    Iterator<IElement> getDependencies();

    long getSizeFootprint();

    int canonicalCompareTo(IElement other);

    boolean isClosed();
}
