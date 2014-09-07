package com.dcrux.haufen.newimpl.elements.array;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.elements.BoxedValue;
import com.dcrux.haufen.newimpl.elements.common.CommonListWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 01/09/14.
 */
public class ArrayElement implements IElement {

    private List<IElement> memory;

    public ArrayElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        this.memory = createMemoryMap();
        CommonListWriter.getInstance().addToList(this.memory, CommonListWriter.getInstance().read(dataInput, hasAdditionalHeader(subtype), elementProvider));
        dataInput.release();
    }

    public ArrayElement() {
        this.memory = createMemoryMap();
    }

    private List<IElement> createMemoryMap() {
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
    public Iterator<IElement> getDependencies() {
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

        ArrayElement that = (ArrayElement) o;

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
    public int canonicalCompareTo(IElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final ArrayElement that = (ArrayElement) other;

        Iterator<IElement> thisIterator = getMemory().iterator();
        Iterator<IElement> thatIterator = that.getMemory().iterator();

        while (thisIterator.hasNext()) {
            final IElement thisEntry = thisIterator.next();
            final IElement thatEntry = thatIterator.next();

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

    private List<IElement> getMemory() {
        assert (this.memory != null);
        return this.memory;
    }

    public Integer getNumberOfElements() {
        return getMemory().size();
    }

    public Iterator<IElement> getIterator() {
        return getMemory().iterator();
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.array;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    public void append(IElement element) {
        getMemory().add(element);
    }

    public void insert(int index, IElement element) {
        getMemory().add(index, element);
    }

    public void removeAll(IElement element) {
        while (getMemory().remove(element)) {
        }
        ;
    }

    public void removeAt(int index) {
        getMemory().remove(index);
    }

    public boolean contains(IElement element) {
        return getMemory().contains(element);
    }
}
