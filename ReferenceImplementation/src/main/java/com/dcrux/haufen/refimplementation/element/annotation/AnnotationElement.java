package com.dcrux.haufen.refimplementation.element.annotation;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.annotation.IAnnotationElement;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.utils.BinaryUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class AnnotationElement extends BaseElement implements IInternalElement, IAnnotationElement {

    private static final int ANNOTATION_MAX_LEN = 256;
    private static final byte[] INITIAL = new byte[]{};
    private byte[] data;

    public AnnotationElement(boolean initialized) {
        if (initialized) {
            this.data = INITIAL;
        }
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        final long len = data.getLength();
        if (len > ANNOTATION_MAX_LEN)
            throw new IllegalArgumentException("Annotation max len is " + ANNOTATION_MAX_LEN);
        this.data = new byte[(int) len];
        data.readFully(this.data);
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        output.write(this.data);
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public String toString() {
        return "Annotation{" +
                Arrays.toString(this.data) +
                '}';
    }

    public long getLength() {
        return this.data.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationElement that = (AnnotationElement) o;
        return Arrays.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public long getSizeFootprint() {
        return this.data.length;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final AnnotationElement that = (AnnotationElement) other;

        return BinaryUtil.getBinaryComparator().compare(this.data, that.data);
    }

    @Override
    public boolean isClosed() {
        return this.data == null;
    }

    @Override
    public void close() throws Exception {
        this.data = null;
    }

    @Override
    public Type getType() {
        return Type.annotation;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public AnnotationElement set(byte[] data) {
        assert (data != null);
        if (data.length > ANNOTATION_MAX_LEN)
            throw new IllegalArgumentException("Annotation max len is " + ANNOTATION_MAX_LEN);
        this.data = data;
        return this;
    }

    @Override
    public byte[] get() {
        return this.data;
    }

}
