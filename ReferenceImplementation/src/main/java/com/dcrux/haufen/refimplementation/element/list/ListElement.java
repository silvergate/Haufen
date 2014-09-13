package com.dcrux.haufen.refimplementation.element.list;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.list.IListElement;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.element.BoxedValue;
import com.dcrux.haufen.refimplementation.element.common.CommonListWriter;
import com.dcrux.haufen.refimplementation.utils.ElementCastUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 01/09/14.
 */
public class ListElement extends BaseElement implements IInternalElement, IListElement {

    private List<IInternalElement> memory;
    private IElementCreator elementCreator;

    public ListElement(boolean initialized, IElementCreator elementCreator) {
        this.elementCreator = elementCreator;
        if (initialized) {
            this.memory = createMemoryMap();
        }
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        this.memory = createMemoryMap();
        CommonListWriter.getInstance().addToList(this.memory, CommonListWriter.getInstance().read(data, hasAdditionalHeader(subtype), elementProvider));
        data.release();
    }

    private List<IInternalElement> createMemoryMap() {
        return new ArrayList<>();
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
        return "ArrayElement{" +
                this.memory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListElement that = (ListElement) o;

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
        final ListElement that = (ListElement) other;

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

    @Override
    public int getNumberOfEntries() {
        return getMemory().size();
    }

    public Iterator<IInternalElement> getIterator() {
        return getMemory().iterator();
    }

    @Override
    public Type getType() {
        return Type.list;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    public ListElement append(IInternalElement element) {
        getMemory().add(element);
        return this;
    }

    public ListElement insert(int index, IInternalElement element) {
        getMemory().add(index, element);
        return this;
    }

    public ListElement removeAll(IInternalElement element) {
        while (getMemory().remove(element)) {
        }
        ;
        return this;
    }

    @Override
    public ListElement clear() {
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

    public IListElement append(IElement element) {
        append(ElementCastUtil.getInstance().cast(element));
        return this;
    }

    @Override
    public IListElement insert(int index, IElement element) {
        insert(index, ElementCastUtil.getInstance().cast(element));
        return this;
    }

    @Override
    public IListElement removeAll(IElement element) {
        removeAll(ElementCastUtil.getInstance().cast(element));
        return this;
    }

    @Override
    public IElement getAccessor(int index) {
        return this.elementCreator.create(Types.INTEGER).set(index);
    }

    @Override
    public boolean remove(IElement element) {
        return getMemory().remove(ElementCastUtil.getInstance().cast(element));
    }

    @Override
    public IListElement add(IElement element) {
        return append(element);
    }

    @Override
    public boolean contains(IElement element) {
        return getCount(element) > 0;
    }

    @Override
    public int getCount(IElement element) {
        return Collections.frequency(getMemory(), ElementCastUtil.getInstance().cast(element));
    }

    @Nullable
    @Override
    public IElement access(IElement accessor) {
        if (!accessor.is(Type.integer))
            return null;
        final int size = getMemory().size();
        if (accessor.as(Types.INTEGER).get() > size - 1 || accessor.as(Types.INTEGER).get() < 0)
            return null;
        return getMemory().get((int) accessor.as(Types.INTEGER).get());
    }
}
