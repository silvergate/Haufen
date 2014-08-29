package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.iface.elements.annotation.IAnnotationElement;
import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.*;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by caelis on 10/08/14.
 */
public class AnnotationElement extends AbstractElement implements IAnnotationElement {
    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.string;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new AnnotationElement((byte[]) payload, false);
        }
    };
    private static Comparator<IDocumentElement> canonicalComparator = new Comparator<IDocumentElement>() {
        @Override
        public int compare(IDocumentElement o1, IDocumentElement o2) {
            final AnnotationElement otherSe = (AnnotationElement) o2;
            final AnnotationElement thisSe = (AnnotationElement) o1;
                /* Sort by length */
            int dif = otherSe.getData().length - thisSe.getData().length;
            if (dif == 0) {
                /* Same length, alpha sort */
                return BinaryUtil.getBinaryComparator().compare(thisSe.data, otherSe.data);
            } else {
                return dif;
            }
        }
    };
    private byte[] data;
    public AnnotationElement(byte[] data) {
        this(data, true);
    }

    private AnnotationElement(byte[] data, boolean initialized) {
        super(initialized);
        assert (data != null);
        this.data = data;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    @Override
    public void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception {
        serializer.write(getBaseType(), getSubtype(), this.data);
    }

    @Override
    public Set<IDocumentElement> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.annotation;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    protected Comparator<IDocumentElement> getSameTypeCanonicalComparator() {
        return canonicalComparator;
    }

    @Override
    protected void initializeInternal(IInverseReferenceMap inverseReferenceMap) {
        /* Nothing to do here */
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public void setData(byte[] data) {
        assert (data!=null);
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationElement that = (AnnotationElement) o;

        if (!Arrays.equals(data, that.data)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
