package com.dcrux.haufen.newimpl.element.bag;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.element.BoxedValue;
import com.dcrux.haufen.newimpl.utils.BinaryUtil;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;
import com.dcrux.haufen.newimpl.utils.Varint;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Created by caelis on 01/09/14.
 */
public class BagElement extends BaseElement implements IInternalElement {

    private static final int NUMBER_OF_UNIQUE_ELEMENTS_ADDITIONAL_HEADER_TS = 32;

    private Map<IInternalElement, Integer> memory;
    private int numberOfElements;
    private int numberOfUniqueElements;

    public BagElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        final boolean hasAdditionalHeader = hasAdditionalHeader(subtype);
        final BagHeader bagHeader = readBagHeaderFromData(dataInput, hasAdditionalHeader);
        final Map<IInternalElement, Integer> memory = fromHeaderToMap(bagHeader, elementProvider, this::createMemoryMap);
        this.numberOfElements = bagHeader.getNumberOfElements();
        this.numberOfUniqueElements = bagHeader.getNumberOfUniqueElements();
        this.memory = memory;
    }

    public BagElement() {
        this.memory = createMemoryMap();
        this.numberOfElements = 0;
        this.numberOfUniqueElements = 0;
    }

    private Map<IInternalElement, Integer> createMemoryMap() {
        return new TreeMap<>((o1, o2) -> o1.canonicalCompareTo(o2));
    }

    private boolean hasAdditionalHeader(byte subtype) {
        return subtype == 1;
    }

    private byte createSubtype() {
        if (getNumberOfUniqueElements() > NUMBER_OF_UNIQUE_ELEMENTS_ADDITIONAL_HEADER_TS)
            return 1;
        else
            return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        BoxedValue<Boolean> outHasAdditionalHeader = new BoxedValue<>();
        write(output, getMemory(), elementIndexProvider,
                elementProvider, outHasAdditionalHeader);
    }

    private BagHeader getBagHeader(IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        final BagHeader bagHeader = new BagHeader();

        getIterator().forEachRemaining(entry -> {
            final int elementNumber = elementIndexProvider.getIndex(entry.getElement());
            bagHeader.addElement(entry.getCount(), elementNumber);
            bagHeader.setNumberOfUniqueElements(bagHeader.getNumberOfUniqueElements() + 1);
            bagHeader.setNumberOfElements(bagHeader.getNumberOfElements() + entry.getCount());
        });
        /* Sort by canonical order within a group */
        for (BagElementGroup elementGroup : bagHeader.getElementCountToElementGroups().values()) {
            Collections.sort(elementGroup.getElementNumbers(), (o1, o2) -> {
                IInternalElement e1 = elementProvider.getElement(o1);
                IInternalElement e2 = elementProvider.getElement(o2);
                return e1.canonicalCompareTo(e2);
            });
        }

        return bagHeader;
    }

    private void write(IDataOutput output, Map<IInternalElement, Integer> elements, IElementIndexProvider elementIndexProvider,
                       IElementProvider elementProvider, BoxedValue<Boolean> outHasAdditionalHeader) {
        final BagHeader header = getBagHeader(elementIndexProvider, elementProvider);

        /* Write content */
        for (Map.Entry<Integer, BagElementGroup> group : header.getElementCountToElementGroups().entrySet()) {
            final int count = group.getKey();
            /* Write group number */
            Varint.writeUnsignedVarInt(count, output);
            /* Write number of element in group */
            Varint.writeUnsignedVarInt(group.getValue().getElementNumbers().size(), output);
            /* Write each element number */
            for (int elementNumber : group.getValue().getElementNumbers()) {
                Varint.writeUnsignedVarInt(elementNumber, output);
            }
        }

        /* Write the actual header (only if enough element) */
        if (header.getNumberOfUniqueElements() > NUMBER_OF_UNIQUE_ELEMENTS_ADDITIONAL_HEADER_TS) {
            output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarInt(header.getNumberOfUniqueElements())));
            output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarInt(header.getNumberOfElements())));
            outHasAdditionalHeader.setValue(true);
        } else {
            outHasAdditionalHeader.setValue(false);
        }
    }

    private BagHeader readBagHeaderFromData(IDataInput dataInput, boolean hasAdditionalHeader) {
        final BagHeader bagHeader = new BagHeader();
        final long intLastElement;
        if (hasAdditionalHeader) {
            final InverseDataInput inverseDataInput = new InverseDataInput(dataInput);
            final int numberOfElements = Varint.readUnsignedVarInt(dataInput);
            final int numberOfUniqueElements = Varint.readUnsignedVarInt(dataInput);
            intLastElement = inverseDataInput.getPosition();
            inverseDataInput.release();
        } else {
            intLastElement = dataInput.getLength() - 1;
        }

        /* Read groups */
        dataInput.seek(0);
        boolean end = false;
        do {
            end = dataInput.getPosition() > intLastElement;
            if (!end) {
                final int count = Varint.readUnsignedVarInt(dataInput);
                final int numberOfElementsInGroup = Varint.readUnsignedVarInt(dataInput);
                for (int i = 0; i < numberOfElementsInGroup; i++) {
                    final int elementNumber = Varint.readUnsignedVarInt(dataInput);
                    bagHeader.addElement(count, elementNumber);
                }
            }
        } while (!end);

        return bagHeader;
    }

    private Map<IInternalElement, Integer> fromHeaderToMap(BagHeader bagHeader, IElementProvider elementProvider,
                                                           Supplier<Map<IInternalElement, Integer>> createMap) {
        final Map<IInternalElement, Integer> result = createMap.get();
        for (Map.Entry<Integer, BagElementGroup> groupEntry : bagHeader.getElementCountToElementGroups().entrySet()) {
            final int count = groupEntry.getKey();
            final BagElementGroup entry = groupEntry.getValue();
            for (int elementNumber : entry.getElementNumbers()) {
                result.put(elementProvider.getElement(elementNumber), count);
            }
        }
        return result;
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        final Iterator<IInternalElement> original = this.memory.keySet().iterator();
        return new Iterator<IInternalElement>() {
            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public IInternalElement next() {
                return original.next();
            }
        };
    }

    @Override
    public String toString() {
        return "BagElement{" +
                getMemory() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagElement that = (BagElement) o;

        if (!memory.equals(that.memory)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return memory.hashCode();
    }


    @Override
    public long getSizeFootprint() {
        return this.numberOfElements;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final BagElement that = (BagElement) other;

        final int uniqueElementsDif = this.getNumberOfUniqueElements() - that.getNumberOfUniqueElements();
        if (uniqueElementsDif != 0)
            return uniqueElementsDif;

        Iterator<Map.Entry<IInternalElement, Integer>> thisIterator = getMemory().entrySet().iterator();
        Iterator<Map.Entry<IInternalElement, Integer>> thatIterator = that.getMemory().entrySet().iterator();

        while (thisIterator.hasNext()) {
            final Map.Entry<IInternalElement, Integer> thisEntry = thisIterator.next();
            final Map.Entry<IInternalElement, Integer> thatEntry = thatIterator.next();

            int elementDif = thisEntry.getKey().canonicalCompareTo(thatEntry.getKey());
            if (elementDif != 0)
                return elementDif;

            int countDif = thisEntry.getValue() - thatEntry.getValue();
            if (countDif != 0)
                return countDif;
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

    private Map<IInternalElement, Integer> getMemory() {
        assert (this.memory != null);
        return this.memory;
    }

    public int add(IInternalElement element) {
        Integer currentCount = getMemory().get(element);
        if (currentCount == null) {
            /* New unique element */
            this.numberOfUniqueElements++;

            currentCount = 0;
        }
        currentCount++;
        getMemory().put(element, currentCount);
        this.numberOfElements++;
        return currentCount;
    }

    public boolean remove(IInternalElement element) {
        Integer currentCount = getMemory().get(element);
        if (currentCount == null)
            return false;
        currentCount--;
        this.numberOfElements--;
        if (currentCount > 0)
            getMemory().put(element, currentCount);
        else {
            getMemory().remove(element);
            /* We lost one unique element */
            this.numberOfUniqueElements--;

        }
        return true;
    }

    public int getCount(IInternalElement element) {
        Integer currentCount = getMemory().get(element);
        if (currentCount == null)
            return 0;
        return currentCount;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Integer getNumberOfUniqueElements() {
        return numberOfUniqueElements;
    }

    public Iterator<BagEntry> getIterator() {
        final Iterator<Map.Entry<IInternalElement, Integer>> baseIterator = this.memory.entrySet().iterator();
        return new Iterator<BagEntry>() {
            @Override
            public boolean hasNext() {
                return baseIterator.hasNext();
            }

            @Override
            public BagEntry next() {
                final Map.Entry<IInternalElement, Integer> entry = baseIterator.next();
                return new BagEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    @Override
    public Type getType() {
        return Type.bag;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

}
