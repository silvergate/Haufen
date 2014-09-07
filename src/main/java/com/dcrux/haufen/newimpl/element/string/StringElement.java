package com.dcrux.haufen.newimpl.element.string;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.element.DataException;
import com.dcrux.haufen.newimpl.utils.BinaryUtil;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;
import com.dcrux.haufen.newimpl.utils.SubInput;
import com.dcrux.haufen.newimpl.utils.Varint;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * Created by caelis on 27/08/14.
 */
public class StringElement extends BaseElement implements IInternalElement {

    private static final int WITH_SIZE_THRESHOLD = 16;

    @Nullable
    private String value;
    @Nullable
    private IDataInput dataInput;
    @Nullable
    private Integer numberOfCodePoints;

    public StringElement(String value) {
        this.value = value;
    }

    public StringElement(IDataInput data) {
        if (data.getLength() > WITH_SIZE_THRESHOLD) {
            final IDataInput inverseInput = new InverseDataInput(data);
            inverseInput.seek(0);
            final int numberOfCodePoints = Varint.readUnsignedVarInt(inverseInput);
            inverseInput.release();
            this.numberOfCodePoints = numberOfCodePoints;

            this.dataInput = new SubInput(data, 0, data.getLength() - inverseInput.getPosition() - 1);
            data.release();
        } else {
            /* Number of characters not included */
            this.dataInput = data;
        }
    }

    @Override
    public Type getType() {
        return Type.string;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        assureNotClosed();

        try {
            final byte[] data = getValue().getBytes("UTF-8");
            output.write(data);

            int codepoints = getNumberOfCodePoints();
            if (codepoints > WITH_SIZE_THRESHOLD) {
                output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarInt(codepoints)));
            }
        } catch (UnsupportedEncodingException e) {
            throw new DataException("Cannot convert string to bytes.");
        }
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        assureNotClosed();
        return Collections.<IInternalElement>emptySet().iterator();
    }

    public String getValue() {
        assureNotClosed();
        if (this.value != null) {
            return this.value;
        } else {
            assert (this.dataInput != null);
            this.dataInput.seek(0);
            final byte[] asBytes = new byte[(int) this.dataInput.getLength()];
            this.dataInput.readFully(asBytes);
            try {
                this.value = new String(asBytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new DataException("Error parsing string", e);
            }
            /* Now remove the data input (cannot have both) */
            assureNoDataInput();
            this.numberOfCodePoints = null;
        }
        return this.value;
    }

    public void setValue(String value) {
        assert (value != null);
        assureNotClosed();
        assureNoDataInput();
        this.value = value;
        this.numberOfCodePoints = null;
    }

    private void assureNoDataInput() {
        if (this.dataInput != null) {
            this.dataInput.release();
            this.dataInput = null;
        }
    }

    public int getNumberOfCodePoints() {
        assureNotClosed();
        if (this.numberOfCodePoints != null)
            return this.numberOfCodePoints;
        else {
            /* Does not seem to be in data input (if so, numberOfCodePoints wouldn't be null) */
            String value = getValue();
            this.numberOfCodePoints = value.codePointCount(0, value.length());
        }
        return this.numberOfCodePoints;
    }

    @Override
    public long getSizeFootprint() {
        assureNotClosed();
        return getNumberOfCodePoints() * 2;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        assureNotClosed();
        int cmp = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (cmp != 0)
            return cmp;
        StringElement otherCast = (StringElement) other;

        /* Compare content */
        PrimitiveIterator.OfInt thisIterator = this.getValue().chars().iterator();
        PrimitiveIterator.OfInt otherIterator = otherCast.getValue().chars().iterator();
        while (true) {
            if (!thisIterator.hasNext()) {
                if (otherIterator.hasNext())
                    throw new AssertionError("Must not happen, since both have the same codepoints number.");
                /* They're equal */
                return 0;
            }
            int thisInt = thisIterator.nextInt();
            int thatInt = otherIterator.nextInt();
            if (thisInt != thatInt)
                return thatInt - thisInt;
        }
    }

    @Override
    public boolean isClosed() {
        return this.value == null & this.dataInput == null;
    }

    private void assureNotClosed() {
        if (isClosed())
            throw new IllegalStateException("Element already closed");
    }

    @Override
    public void close() throws Exception {
        assureNoDataInput();
        this.value = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringElement that = (StringElement) o;

        if (!getValue().equals(that.getValue())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public String toString() {
        return "StringElement{" +
                getValue() +
                '}';
    }
}
