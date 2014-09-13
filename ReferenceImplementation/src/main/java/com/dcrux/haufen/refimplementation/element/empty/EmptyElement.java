package com.dcrux.haufen.refimplementation.element.empty;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.empty.IEmptyElement;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class EmptyElement extends BaseElement implements IInternalElement, IEmptyElement {

    private static EmptyElement singleton;

    public EmptyElement() {
    }

    public static EmptyElement getSingleton() {
        if (singleton == null)
            singleton = new EmptyElement();
        return singleton;
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
    }

    @Override
    public Type getType() {
        return Type.empty;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return 1;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        return 0;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return EmptyElement.class.hashCode();
    }

    @Override
    public String toString() {
        return "EmptyElement{}";
    }

}
