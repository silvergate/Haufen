package com.dcrux.haufen.refimplementation.element.string;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.string.IStringElement;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.element.DataException;
import com.dcrux.haufen.refimplementation.utils.BinaryUtil;
import com.dcrux.haufen.refimplementation.utils.InverseDataInput;
import com.dcrux.haufen.refimplementation.utils.SubInput;
import com.dcrux.haufen.refimplementation.utils.Varint;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * Created by caelis on 27/08/14.
 */
public class StringElement extends BaseElement implements IInternalElement, IStringElement {

    private static final int WITH_SIZE_THRESHOLD = 16;

    @Nullable
    private String value;
    @Nullable
    private IDataInput dataInput;
    private int numberOfCodePoints = -1;

    public StringElement(String value) {
        this.value = value;
    }

    public StringElement() {
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
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
            this.numberOfCodePoints = -1;
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
            final byte[] data = get().getBytes("UTF-8");
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

    @Override
    public String get() {
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
        }
        return this.value;
    }

    @Override
    public int getNumberOfCodePoints() {
        assureNotClosed();
        if (this.numberOfCodePoints == -1) {
            final String value = get();
            this.numberOfCodePoints = value.codePointCount(0, value.length());
        }
        return this.numberOfCodePoints;
    }

    @Override
    public IStringElement set(String value) {
        assert (value != null);
        assureNotClosed();
        assureNoDataInput();
        this.value = value;
        this.numberOfCodePoints = -1;
        return this;
    }

    private void assureNoDataInput() {
        if (this.dataInput != null) {
            this.dataInput.release();
            this.dataInput = null;
        }
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
        PrimitiveIterator.OfInt thisIterator = this.get().chars().iterator();
        PrimitiveIterator.OfInt otherIterator = otherCast.get().chars().iterator();
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

        if (!get().equals(that.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public String toString() {
        return "StringElement{'" +
                get() +
                "'}";
    }
}
