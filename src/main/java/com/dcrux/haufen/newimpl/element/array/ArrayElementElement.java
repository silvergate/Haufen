package com.dcrux.haufen.newimpl.element.array;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.array.IArrayElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.element.BoxedValue;
import com.dcrux.haufen.newimpl.element.common.CommonListWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 01/09/14.
 */
public class ArrayElementElement extends BaseElement implements IInternalElement, IArrayElement {

    private List<IInternalElement> memory;

    public ArrayElementElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        this.memory = createMemoryMap();
        CommonListWriter.getInstance().addToList(this.memory, CommonListWriter.getInstance().read(dataInput, hasAdditionalHeader(subtype), elementProvider));
        dataInput.release();
    }

    public ArrayElementElement() {
        this.memory = createMemoryMap();
    }

    private List<IInternalElement> createMemoryMap() {
        return new ArrayList<>();
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
        return "ArrayElement{" +
                this.memory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayElementElement that = (ArrayElementElement) o;

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
        final ArrayElementElement that = (ArrayElementElement) other;

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

    private List<IInternalElement> getMemory() {
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
        return Type.array;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    public void append(IInternalElement element) {
        getMemory().add(element);
    }

    public void insert(int index, IInternalElement element) {
        getMemory().add(index, element);
    }

    public void removeAll(IInternalElement element) {
        while (getMemory().remove(element)) {
        }
        ;
    }

    @Override
    public ArrayElementElement clear() {
        getMemory().clear();
        return this;
    }

    @Override
    public Iterator<IElement> iterator() {
        final Iterator<IInternalElement> original = getIterator();
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

    public void removeAt(int index) {
        getMemory().remove(index);
    }

    public boolean contains(IInternalElement element) {
        return getMemory().contains(element);
    }
}
