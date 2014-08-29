package com.dcrux.haufen.newimpl;

import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

/**
 * Created by caelis on 27/08/14.
 */
public interface IElement extends IEqualsHashcode, AutoCloseable {
    BaseType getBaseType();
    byte getSubtype();
    void write(IDataOutput output) throws IOException;
    Set<IElement> getDependencies();
    long getSizeFootprint() throws IOException;
    int canonicalCompareTo(IElement other) throws IOException;
    boolean isClosed();
}
