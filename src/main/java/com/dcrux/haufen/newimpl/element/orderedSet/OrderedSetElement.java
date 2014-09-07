package com.dcrux.haufen.newimpl.element.orderedSet;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.element.BoxedValue;
import com.dcrux.haufen.newimpl.element.common.CommonListWriter;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedSetElement extends BaseElement implements IInternalElement {

    private Set<IInternalElement> memory;

    public OrderedSetElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        this.memory = createMemoryMap();
        CommonListWriter.getInstance().addToList(this.memory, CommonListWriter.getInstance().read(dataInput, hasAdditionalHeader(subtype), elementProvider));
        dataInput.release();
    }

    public OrderedSetElement() {
        this.memory = createMemoryMap();
    }

    private Set<IInternalElement> createMemoryMap() {
        return new LinkedHashSet<>();
    }

    private boolean hasAdditionalHeader(byte subtype) {
        return subtype == 1;
    }

    private byte createSubtype() {
        if (CommonListWriter.getInstance().hasAdditionalHeader(getNumberOfElements()))
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
        return getNumberOfElements();
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

    public Integer getNumberOfElements() {
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
}
