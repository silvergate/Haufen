package com.dcrux.haufen.refimplementation.element.bool;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.bool.IBoolElement;
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
public class BoolElement extends BaseElement implements IInternalElement, IBoolElement {

    private Boolean value;

    public BoolElement(boolean initialized) {
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        byte dataByte = data.readByte();
        if (dataByte == 1)
            this.value = true;
        else if (dataByte == 0)
            this.value = false;
        else
            throw new IllegalStateException("Invalid boolean value");
        data.release();
    }

    @Override
    public IBoolElement set(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean get() {
        return this.value;
    }

    @Override
    public Type getType() {
        return Type.bool;
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
