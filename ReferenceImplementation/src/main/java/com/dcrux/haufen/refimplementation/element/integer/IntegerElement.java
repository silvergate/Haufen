package com.dcrux.haufen.refimplementation.element.integer;

import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.integer.IntegerType;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.utils.Subtype;
import com.dcrux.haufen.refimplementation.utils.Varint;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class IntegerElement extends BaseElement implements IInternalElement, IIntegerElement {

    private long value;
    private byte subtype;

    public IntegerElement() {
        this(0l, (byte) 0);
    }

    public IntegerElement(long value, byte subtype) {
        this.value = value;
        this.subtype = subtype;
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        IntegerElement integerElement = new IntegerElement(0, subtype);
        switch (integerElement.getIntegerType()) {
            case signed:
                value = Varint.readSignedVarLong(data);
                set(value);
                break;
            case unsigned:
                value = Varint.readUnsignedVarLong(data);
                uset(value);
                break;
        }
    }

    @Override
    public long get() {
        return value;
    }

    @Override
    public IntegerElement set(long value) {
        this.value = value;
        setType(IntegerType.signed);
        return this;
    }

    @Override
    public IIntegerElement uset(long value) {
        this.value = value;
        setType(IntegerType.unsigned);
        return this;
    }

    @Override
    public IntegerType getIntegerType() {
        if (Subtype.isFlag0(this.subtype))
            return IntegerType.unsigned;
        else
            return IntegerType.signed;
    }

    @Override
    public IIntegerElement setIntegerType(IntegerType integerType) {
        switch (integerType) {
            case signed:
                this.subtype = 0;
                break;
            case unsigned:
                this.subtype = Subtype.setFlag0(this.subtype);
                break;
        }
        return this;
    }

    @Override
    public com.dcrux.haufen.Type getType() {
        return com.dcrux.haufen.Type.integer;
    }

    public void setType(IntegerType integerType) {
        switch (integerType) {
            case signed:
                this.subtype = Subtype.unsetFlag0(this.subtype);
                break;
            case unsigned:
                this.subtype = Subtype.setFlag0(this.subtype);
                break;
        }
    }

    @Override
    public byte getSubtype() {
        return this.subtype;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        switch (getIntegerType()) {
            case signed:
                Varint.writeSignedVarLong(get(), output);
                break;
            case unsigned:
                Varint.writeUnsignedVarLong(get(), output);
                break;
        }
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return 2;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        switch (getIntegerType()) {
            case signed:
                return Long.compare(((IntegerElement) other).get(), get());
            case unsigned:
                return Long.compareUnsigned(((IntegerElement) other).get(), get());
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
        if (getIntegerType() == IntegerType.signed) {
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
