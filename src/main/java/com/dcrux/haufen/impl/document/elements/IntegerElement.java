package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.*;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by caelis on 12/08/14.
 */
public class IntegerElement extends AbstractElement {

    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.integer;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new IntegerElement((Long) payload, false);
        }
    };
    private final long value;
    private static Comparator<IDocumentElement> sameTypeCanonicalComparator =
            new Comparator<IDocumentElement>() {
                @Override
                public int compare(IDocumentElement o1, IDocumentElement o2) {
                    IntegerElement ie1 = (IntegerElement) o1;
                    IntegerElement ie2 = (IntegerElement) o2;
                    return Long.compare(ie1.value, ie2.value);
                }
            };


    public IntegerElement(long value) {
        this(value, true);
    }

    private IntegerElement(long value, boolean initialized) {
        super(initialized);
        this.value = value;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    public long getValue() {
        return value;
    }

    @Override
    protected Comparator<IDocumentElement> getSameTypeCanonicalComparator() {
        return sameTypeCanonicalComparator;
    }

    @Override
    protected void initializeInternal(IInverseReferenceMap inverseReferenceMap) {
        /* Nothing to do here */
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.integer;
    }

    @Override
    public void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception {
        serializer.write(getBaseType(), getSubtype(), this.value);
    }

    @Override
    public Set<IDocumentElement> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerElement that = (IntegerElement) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return "IntegerElement{" +
                "value=" + value +
                '}';
    }
}
