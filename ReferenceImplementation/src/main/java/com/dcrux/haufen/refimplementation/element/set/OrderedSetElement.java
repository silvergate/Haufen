package com.dcrux.haufen.refimplementation.element.set;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.set.IBaseSetElement;
import com.dcrux.haufen.element.set.IOrderedSetElement;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.element.BoxedValue;
import com.dcrux.haufen.refimplementation.element.common.CommonListWriter;
import com.dcrux.haufen.refimplementation.utils.ElementCastUtil;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedSetElement extends BaseElement implements IInternalElement, IOrderedSetElement {

    private Set<IInternalElement> memory;

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        this.memory = createMemoryMap();
        CommonListWriter.getInstance().addToList(this.memory, CommonListWriter.getInstance().read(data, hasAdditionalHeader(subtype), elementProvider));
        data.release();
    }

    public OrderedSetElement(boolean initialized) {
        if (initialized) {
            this.memory = createMemoryMap();
        }
    }

    private Set<IInternalElement> createMemoryMap() {
        return new LinkedHashSet<>();
    }

    private boolean hasAdditionalHeader(byte subtype) {
        return subtype == 1;
    }

    private byte createSubtype() {
        if (CommonListWriter.getInstance().hasAdditionalHeader(getNumberOfEntries()))
            return 1;
        else
            return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        BoxedValue<Boolean> withAdditionalHeaders = new BoxedValue<>();
        CommonListWriter.getInstance().write(output, elementIndexProvider, elementProvider, withAdditionalHeaders, this.memory.iterator());
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return this.memory.iterator();
    }

    @Override
    public String toString() {
        return "OrderedSetElement{" +
                this.memory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedSetElement that = (OrderedSetElement) o;

        if (!memory.equals(that.memory)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return memory.hashCode();
    }

    @Override
    public long getSizeFootprint() {
        return getNumberOfEntries();
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final OrderedSetElement that = (OrderedSetElement) other;

        Iterator<IInternalElement> thisIterator = getMemory().iterator();
        Iterator<IInternalElement> thatIterator = that.getMemory().iterator();

        while (thisIterator.hasNext()) {
            final IInternalElement thisEntry = thisIterator.next();
            final IInternalElement thatEntry = thatIterator.next();

            int elementDif = thisEntry.canonicalCompareTo(thatEntry);
            if (elementDif != 0)
                return elementDif;
        }
        return 0;
    }

    @Override
    public boolean isClosed() {
        return this.memory == null;
    }

    @Override
    public void close() throws Exception {
        this.memory = null;
    }

    private Set<IInternalElement> getMemory() {
        assert (this.memory != null);
        return this.memory;
    }

    @Override
    public int getNumberOfEntries() {
        return getMemory().size();
    }

    public Iterator<IInternalElement> getIterator() {
        return getMemory().iterator();
    }

    @Override
    public Type getType() {
        return Type.orderedSet;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    public boolean add(IInternalElement element) {
        return getMemory().add(element);
    }

    public boolean remove(IInternalElement element) {
        return getMemory().remove(element);
    }

    public boolean contains(IInternalElement element) {
        return getMemory().remove(element);
    }

    @Override
    public IBaseSetElement clear() {
        getMemory().clear();
        return this;
    }

    @Override
    public Iterator<IElement> iterator() {
        final Iterator<IInternalElement> original = getMemory().iterator();
        return new Iterator<IElement>() {
            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public IElement next() {
                return original.next();
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return getMemory().isEmpty();
    }

    @Override
    public boolean remove(IElement element) {
        return getMemory().remove(ElementCastUtil.getInstance().cast(element));
    }

    @Override
    public IBaseSetElement add(IElement element) {
        add(ElementCastUtil.getInstance().cast(element));
        return this;
    }

    @Override
    public boolean contains(IElement element) {
        return getMemory().contains(ElementCastUtil.getInstance().cast(element));
    }

    @Override
    public int getCount(IElement element) {
        /* Since it's a set, can only return 0 or 1. */
        if (contains(element))
            return 1;
        else
            return 0;
    }
}
