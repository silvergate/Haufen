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
public class BooleanElement extends AbstractElement {

    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.integer;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new BooleanElement((Boolean) payload, false);
        }
    };
    private final boolean value;
    private static Comparator<IDocumentElement> sameTypeCanonicalComparator =
            new Comparator<IDocumentElement>() {
                @Override
                public int compare(IDocumentElement o1, IDocumentElement o2) {
                    BooleanElement ie1 = (BooleanElement) o1;
                    BooleanElement ie2 = (BooleanElement) o2;
                    return Boolean.compare(ie1.value, ie2.value);
                }
            };


    public BooleanElement(boolean value) {
        this(value, true);
    }

    private BooleanElement(boolean value, boolean initialized) {
        super(initialized);
        this.value = value;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    public boolean getValue() {
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
        return BaseType.bool;
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

        BooleanElement that = (BooleanElement) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public String toString() {
        return "BooleanElement{" +
                "value=" + value +
                '}';
    }
}
