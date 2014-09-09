package com.dcrux.haufen.refimplementation;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IEqualsHashcode;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;

import java.util.Iterator;

/**
 * Created by caelis on 27/08/14.
 */
public interface IInternalElement extends IEqualsHashcode, AutoCloseable, IElement {
    Type getType();

    byte getSubtype();

    void initialize(IDataInput data, byte subtype, IElementProvider elementProvider);

    void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider);

    Iterator<IInternalElement> getDependencies();

    long getSizeFootprint();

    int canonicalCompareTo(IInternalElement other);

    boolean isClosed();
}
