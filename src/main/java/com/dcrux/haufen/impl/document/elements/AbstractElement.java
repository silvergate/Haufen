package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.document.IInverseReferenceMap;
import com.dcrux.haufen.impl.document.ISecondStageDocumentElementFactory;

import java.util.Comparator;

/**
 * Created by caelis on 10/08/14.
 */
public abstract class AbstractElement implements IDocumentElement, ISecondStageDocumentElementFactory {

    private boolean initialized;
    private Comparator<IDocumentElement> canonicalComparator = new Comparator<IDocumentElement>() {
        @Override
        public int compare(IDocumentElement o1, IDocumentElement o2) {
            if (o1.getBaseType().equals(o2.getBaseType())) {
                /* Cannot compare here... (subtype should compare) */
                if (o1.equals(o2))
                    return 0;
                else
                    return getSameTypeCanonicalComparator().compare(o1, o2);
            } else {
                /* Sort by type and subtype */
                int x1 = (o1.getBaseType().getCode() * 256 + o1.getSubtype());
                int x2 = (o2.getBaseType().getCode() * 256 + o2.getSubtype());
                return x1 - x2;
            }
        }
    };

    @Override
    public byte getSubtype() {
        return 0;
    }

    protected AbstractElement() {
        this.initialized = true;
    }

    protected AbstractElement(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public Comparator<IDocumentElement> getCanonicalComparator() {
        return canonicalComparator;
    }

    protected abstract Comparator<IDocumentElement> getSameTypeCanonicalComparator();

    @Override
    public void initialize(IInverseReferenceMap inverseReferenceMap) {
        if (this.initialized) {
            throw new IllegalStateException("Element does not need initialization or has already been initialized.");
        }
        initializeInternal(inverseReferenceMap);
        this.initialized = true;
    }

    protected abstract void initializeInternal(IInverseReferenceMap inverseReferenceMap);
}
