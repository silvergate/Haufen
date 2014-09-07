package com.dcrux.haufen.newimpl.elements.bool;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BaseCanonicalComparatorUtil;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class BoolElement implements IElement {

    private Boolean value;

    public BoolElement() {
        this.value = false;
    }

    public BoolElement(IDataInput dataInput) {
        byte data = dataInput.readByte();
        if (data == 1)
            this.value = true;
        else if (data == 0)
            this.value = false;
        else
            throw new IllegalStateException("Invalid boolean value");
        dataInput.release();
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.bool;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        if (value)
            output.writeByte(0);
        else
            output.writeByte(1);
    }

    @Override
    public Iterator<IElement> getDependencies() {
        return Collections.<IElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return 1;
    }

    @Override
    public int canonicalCompareTo(IElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        BoolElement that = (BoolElement) other;
        return Boolean.compare(this.value, that.value);
    }

    @Override
    public boolean isClosed() {
        return this.value == null;
    }

    @Override
    public void close() throws Exception {
        this.value = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoolElement that = (BoolElement) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BoolElement{" +
                this.value +
                '}';
    }
}
