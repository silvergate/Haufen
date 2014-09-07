package com.dcrux.haufen.newimpl.elements.integer;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.utils.Subtype;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class IntegerElement implements IElement {

    private long value;
    private byte subtype;

    public IntegerElement() {
        this(0l, (byte) 0);
    }

    public IntegerElement(long value) {
        this(value, (byte) 0);
        setType(Type.signed);
    }

    public IntegerElement(long value, byte subtype) {
        this.value = value;
        this.subtype = subtype;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Type getType() {
        if (Subtype.isFlag0(this.subtype))
            return Type.signed;
        else
            return Type.unsigned;
    }

    public void setType(Type type) {
        switch (type) {
            case signed:
                this.subtype = Subtype.unsetFlag0(this.subtype);
                break;
            case unsigned:
                this.subtype = Subtype.setFlag0(this.subtype);
                break;
        }
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.integer;
    }

    @Override
    public byte getSubtype() {
        return this.subtype;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        switch (getType()) {
            case signed:
                Varint.writeSignedVarLong(getValue(), output);
                break;
            case unsigned:
                Varint.writeUnsignedVarLong(getValue(), output);
                break;
        }
    }

    @Override
    public Iterator<IElement> getDependencies() {
        return Collections.<IElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return 2;
    }

    @Override
    public int canonicalCompareTo(IElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        switch (getType()) {
            case signed:
                return Long.compare(((IntegerElement) other).getValue(), getValue());
            case unsigned:
                return Long.compareUnsigned(((IntegerElement) other).getValue(), getValue());
        }
        throw new IllegalStateException("Unknown type");
    }

    @Override
    public boolean isClosed() {
        return this.subtype == -1;
    }

    @Override
    public void close() throws Exception {
        this.subtype = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerElement that = (IntegerElement) o;

        if (subtype != that.subtype) return false;
        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (value ^ (value >>> 32));
        result = 31 * result + (int) subtype;
        return result;
    }

    @Override
    public String toString() {
        if (getType() == Type.signed) {
            return "IntegerElement{" +
                    +value +
                    "|s" +
                    '}';
        } else {
            return "IntegerElement{" +
                    +value +
                    "|u" +
                    '}';
        }
    }
}
