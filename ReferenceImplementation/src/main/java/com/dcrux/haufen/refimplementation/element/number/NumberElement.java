package com.dcrux.haufen.refimplementation.element.number;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.number.INumberElement;
import com.dcrux.haufen.element.number.NumberType;
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
public class NumberElement extends BaseElement implements IInternalElement, INumberElement {

    private long mantissa;
    private long exponent;
    private byte subtype;

    public NumberElement() {
        this(0l, 0l, (byte) 0);
    }

    public NumberElement(long mantissa, long exponent, byte subtype) {
        this.mantissa = mantissa;
        this.exponent = exponent;
        this.subtype = subtype;
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        NumberElement numberElement = new NumberElement(0, 0, subtype);
        switch (numberElement.getNumberType()) {
            case base2:
                long mantissa = Varint.readSignedVarLong(data);
                long exponent = Varint.readSignedVarLong(data);
                set(mantissa, exponent);
                break;
            case base10:
                long mantissaDecimal = Varint.readSignedVarLong(data);
                long exponentDecimal = Varint.readSignedVarLong(data);
                setDecimal(mantissaDecimal, exponentDecimal);
                break;
        }
    }

    @Override
    public long getMantissa() {
        return mantissa;
    }

    @Override
    public long getExponent() {
        return this.exponent;
    }

    private void checkForUniqueZero(long mantissa, long exponent) {
        if ((mantissa == 0) && (exponent != 0)) {
            throw new NumberFormatException("If mantissa is 0, exponent has to be 0 too.");
        }
    }

    @Override
    public NumberElement set(long mantissa, long exponent) {
        checkForUniqueZero(mantissa, exponent);
        this.mantissa = mantissa;
        this.exponent = exponent;
        setNumberType(NumberType.base2);
        return this;
    }

    @Override
    public NumberElement setDecimal(long mantissa, long exponent) {
        checkForUniqueZero(mantissa, exponent);
        this.mantissa = mantissa;
        this.exponent = exponent;
        setNumberType(NumberType.base10);
        return this;
    }

    @Override
    public NumberType getNumberType() {
        if (Subtype.isFlag0(this.subtype))
            return NumberType.base10;
        else
            return NumberType.base2;
    }

    @Override
    public INumberElement setNumberType(NumberType numberType) {
        switch (numberType) {
            case base2:
                this.subtype = 0;
                break;
            case base10:
                this.subtype = Subtype.setFlag0(this.subtype);
                break;
        }
        return this;
    }

    @Override
    public com.dcrux.haufen.Type getType() {
        return Type.number;
    }

    @Override
    public byte getSubtype() {
        return this.subtype;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        Varint.writeSignedVarLong(this.mantissa, output);
        Varint.writeSignedVarLong(this.exponent, output);
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        //TODO: Need to be more precise?
        if (this.exponent < Integer.MAX_VALUE && this.mantissa < Integer.MAX_VALUE)
            return 4;
        else
            return 8;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final NumberElement that = (NumberElement) other;

        /* First by base */
        if (that.getNumberType() != this.getNumberType()) {
            if (that.getNumberType() == NumberType.base10)
                return -1;
            else
                return 1;
        }

        int expoentDif = Long.compare(this.exponent, that.exponent);
        if (expoentDif != 0)
            return expoentDif;

        int mantisseDif = Long.compare(this.mantissa, that.mantissa);
        if (mantisseDif != 0)
            return mantisseDif;

        return 0;
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

        NumberElement that = (NumberElement) o;

        if (exponent != that.exponent) return false;
        if (mantissa != that.mantissa) return false;
        if (subtype != that.subtype) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (mantissa ^ (mantissa >>> 32));
        result = 31 * result + (int) (exponent ^ (exponent >>> 32));
        result = 31 * result + (int) subtype;
        return result;
    }

    @Override
    public String toString() {
        final String number;
        if (getNumberType() == NumberType.base10) {
            //TODO: Inexact
            if ((mantissa < Integer.MAX_VALUE && mantissa >= -Integer.MIN_VALUE)
                    && (exponent < 4 && exponent > -5)) {
                number = String.valueOf(mantissa * Math.pow(10, exponent));
            } else
                number = mantissa + "e" + this.exponent;
        } else {
            //TODO: Inexact
            if ((mantissa < Math.pow(2, 51) && mantissa >= -Math.pow(2, 51))
                    && (exponent < Math.pow(2, 8) && exponent >= -Math.pow(2, 8))) {
                number = String.valueOf(mantissa * Math.pow(2, exponent));
            } else
                number = mantissa + "*2^" + this.exponent;
        }
        return "NumberElement{"
                + number +
                "}";
    }
}
