package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.*;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by caelis on 10/08/14.
 */
public class BagElement extends AbstractElement {

    private static final Logger LOG = Logger.getLogger(BagElement.class.getName());
    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.string;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new BagElement((List<Integer>) payload);
        }
    };
    private static Comparator<IDocumentElement> canonicalComparator = new Comparator<IDocumentElement>() {
        @Override
        public int compare(IDocumentElement o1, IDocumentElement o2) {
            final BagElement otherSe = (BagElement) o2;
            final BagElement thisSe = (BagElement) o1;

            /* Sort by length */
            int dif = otherSe.elementList.size() - thisSe.elementList.size();
            if (dif == 0) {
                /* Same length, sort by elements */
                /* First do sort */
                thisSe.sort();
                otherSe.sort();
                for (int i = 0; i < thisSe.elementList.size(); i++) {
                    IDocumentElement thisElement = thisSe.elementList.get(i);
                    IDocumentElement otherElement = otherSe.elementList.get(i);
                    int elementDif = thisElement.getCanonicalComparator().compare(thisElement, otherElement);
                    if (elementDif != 0) {
                        return elementDif;
                    }
                }

                /* This should not happen, since if we're here the two elements are equal */
                LOG.warning("This should not happen, since if we're here the two elements are equal");
                return 0;
            } else {
                return dif;
            }
        }
    };
    private final List<IDocumentElement> elementList = new ArrayList<>();
    private final List<Integer> references;

    public BagElement() {
        super(true);
        this.references = null;
    }

    private BagElement(List<Integer> references) {
        super(false);
        this.references = references;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    private void sort() {
        Collections.sort(this.elementList, new Comparator<IDocumentElement>() {
            @Override
            public int compare(IDocumentElement o1, IDocumentElement o2) {
                return o1.getCanonicalComparator().compare(o1, o2);
            }
        });
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.bag;
    }

    @Override
    public void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception {
        /* Need a sort here, since content of child elements might have changed */
        sort();
        List<Integer> references = new ArrayList<>();
        for (IDocumentElement element : this.elementList) {
            final int reference = referenceMap.getReference(element);
            references.add(reference);
        }
        serializer.write(getBaseType(), getSubtype(), references);
    }

    public void add(IDocumentElement element) {
        this.elementList.add(element);
    }

    public boolean remove(IDocumentElement element) {
        return this.elementList.remove(element);
    }

    public boolean removeAll(IDocumentElement element) {
        return this.elementList.removeAll(Collections.singleton(element));
    }

    public int getCount(IDocumentElement element) {
        throw new IllegalArgumentException("Implement ME!");
    }

    @Override
    public Set<IDocumentElement> getDependencies() {
        Set<IDocumentElement> elementsAsSet = new HashSet<>();
        elementsAsSet.addAll(this.elementList);
        return elementsAsSet;
    }

    @Override
    protected Comparator<IDocumentElement> getSameTypeCanonicalComparator() {
        return canonicalComparator;
    }

    @Override
    protected void initializeInternal(IInverseReferenceMap inverseReferenceMap) {
        for (final int reference : this.references) {
            add(inverseReferenceMap.getElement(reference));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagElement that = (BagElement) o;

        if (!elementList.equals(that.elementList)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return listHashCode();
    }

    public int listHashCode() {
        int hashCode = 1;
        for (IDocumentElement e : this.elementList) {
            /* Ignore myself (prevent cycle) */
            if (e != this) {
                hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return "BagElement{" +
                "elementList=" + listToString() +
                '}';
    }

    public String listToString() {
        Iterator<IDocumentElement> it = this.elementList.iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            IDocumentElement e = it.next();
            sb.append(e == this ? "[this]" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
