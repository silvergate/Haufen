package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.*;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by caelis on 10/08/14.
 */
public class StringElement extends AbstractElement {
    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.string;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new StringElement((String) payload, false);
        }
    };
    private static Comparator<IDocumentElement> canonicalComparator = new Comparator<IDocumentElement>() {
        @Override
        public int compare(IDocumentElement o1, IDocumentElement o2) {
            final StringElement otherSe = (StringElement) o2;
            final StringElement thisSe = (StringElement) o1;
                /* Sort by length */
            int dif = otherSe.getString().length() - thisSe.getString().length();
            if (dif == 0) {
                /* Same length, alpha sort */
                return thisSe.getString().compareTo(otherSe.getString());
            } else {
                return dif;
            }
        }
    };
    private String string;
    public StringElement(String string) {
        this(string, true);
    }

    private StringElement(String string, boolean initialized) {
        super(initialized);
        assert (string != null);
        this.string = string;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    @Override
    public void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception {
        serializer.write(getBaseType(), getSubtype(), this.string);
    }

    @Override
    public Set<IDocumentElement> getDependencies() {
        return Collections.emptySet();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        assert (string != null);
        this.string = string;
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.string;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringElement that = (StringElement) o;

        if (!string.equals(that.string)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public String toString() {
        return "StringElement{" +
                "string='" + string + '\'' +
                '}';
    }
}
