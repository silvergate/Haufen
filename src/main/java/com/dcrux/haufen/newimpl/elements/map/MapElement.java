package com.dcrux.haufen.newimpl.elements.map;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.elements.BoxedValue;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Created by caelis on 01/09/14.
 */
public class MapElement implements IElement {

    private static final int NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER = 32;

    private Map<IElement, IElement> memory;
    private int numberOfElements;

    public MapElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        final boolean hasAdditionalHeader = hasAdditionalHeader(subtype);
        final MapHeader mapHeader = readBagHeaderFromData(dataInput, hasAdditionalHeader);
        final Map<IElement, IElement> memory = fromHeaderToMap(mapHeader, elementProvider, this::createMemoryMap);
        this.numberOfElements = mapHeader.getNumberOfElements();
        this.memory = memory;
    }

    public MapElement() {
        this.memory = createMemoryMap();
        this.numberOfElements = 0;
    }

    protected boolean isOrdered() {
        return false;
    }

    private Map<IElement, IElement> createMemoryMap() {
        if (isOrdered()) {
            return new LinkedHashMap<>();
        } else {
            return new TreeMap<>((o1, o2) -> o1.canonicalCompareTo(o2));
        }
    }

    private boolean hasAdditionalHeader(byte subtype) {
        return subtype == 1;
    }

    private byte createSubtype() {
        if (getNumberOfElements() > NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER)
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

    private MapHeader getBagHeader(IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        final MapHeader mapHeader = new MapHeader();

        getIterator().forEachRemaining(entry -> {
            final IElement key = entry.getKey();
            final IElement value = entry.getValue();
            mapHeader.getMap().put(elementIndexProvider.getIndex(key), elementIndexProvider.getIndex(value));
        });

        return mapHeader;
    }

    private void write(IDataOutput output, Map<IElement, IElement> elements, IElementIndexProvider elementIndexProvider,
                       IElementProvider elementProvider, BoxedValue<Boolean> outHasAdditionalHeader) {
        final MapHeader header = getBagHeader(elementIndexProvider, elementProvider);

        /* Write content */
        for (Map.Entry<Integer, Integer> entry : header.getMap().entrySet()) {
            final int key = entry.getKey();
            final int value = entry.getValue();
            Varint.writeUnsignedVarInt(key, output);
            Varint.writeUnsignedVarInt(value, output);
        }

        /* Write the actual header (only if enough elements) */
        if (header.getNumberOfElements() > NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER) {
            output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarInt(header.getMap().size())));
            outHasAdditionalHeader.setValue(true);
        } else {
            outHasAdditionalHeader.setValue(false);
        }
    }

    private MapHeader readBagHeaderFromData(IDataInput dataInput, boolean hasAdditionalHeader) {
        final MapHeader mapHeader = new MapHeader();
        final long intLastElement;
        if (hasAdditionalHeader) {
            final InverseDataInput inverseDataInput = new InverseDataInput(dataInput);
            final int numberOfElements = Varint.readUnsignedVarInt(dataInput);
            intLastElement = inverseDataInput.getPosition();
            inverseDataInput.release();
        } else {
            intLastElement = dataInput.getLength() - 1;
        }

        /* Read groups */
        dataInput.seek(0);
        boolean end;
        do {
            end = dataInput.getPosition() > intLastElement;
            if (!end) {
                final int key = Varint.readUnsignedVarInt(dataInput);
                final int value = Varint.readUnsignedVarInt(dataInput);
                mapHeader.getMap().put(key, value);
            }
        } while (!end);

        return mapHeader;
    }

    private Map<IElement, IElement> fromHeaderToMap(MapHeader mapHeader, IElementProvider elementProvider,
                                                    Supplier<Map<IElement, IElement>> createMap) {
        final Map<IElement, IElement> result = createMap.get();
        for (Map.Entry<Integer, Integer> entry : mapHeader.getMap().entrySet()) {
            final int key = entry.getKey();
            final int value = entry.getValue();
            result.put(elementProvider.getElement(key), elementProvider.getElement(value));
        }
        return result;
    }

    @Override
    public Iterator<IElement> getDependencies() {
        final Iterator<IElement> originalKeys = this.memory.keySet().iterator();
        final Iterator<IElement> originalValues = this.memory.values().iterator();
        return new Iterator<IElement>() {
            @Override
            public boolean hasNext() {
                return originalKeys.hasNext() || originalValues.hasNext();
            }

            @Override
            public IElement next() {
                if (originalKeys.hasNext())
                    return originalKeys.next();
                else
                    return originalValues.next();
            }
        };
    }

    @Override
    public String toString() {
        if (isOrdered()) {
            return "OrderedMapElement{" +
                    getMemory() +
                    '}';
        } else {
            return "MapElement{" +
                    getMemory() +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapElement that = (MapElement) o;

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
    public int canonicalCompareTo(IElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final MapElement that = (MapElement) other;

        Iterator<Map.Entry<IElement, IElement>> thisIterator = getMemory().entrySet().iterator();
        Iterator<Map.Entry<IElement, IElement>> thatIterator = that.getMemory().entrySet().iterator();

        while (thisIterator.hasNext()) {
            final Map.Entry<IElement, IElement> thisEntry = thisIterator.next();
            final Map.Entry<IElement, IElement> thatEntry = thatIterator.next();

            int elementDif = thisEntry.getKey().canonicalCompareTo(thatEntry.getKey());
            if (elementDif != 0)
                return elementDif;

            int countDif = thisEntry.getValue().canonicalCompareTo(thatEntry.getValue());
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

    private Map<IElement, IElement> getMemory() {
        assert (this.memory != null);
        return this.memory;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Iterator<Map.Entry<IElement, IElement>> getIterator() {
        return this.memory.entrySet().iterator();
    }

    @Override
    public BaseType getBaseType() {
        if (isOrdered())
            return BaseType.orderedMap;
        else
            return BaseType.map;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    private void assureOrdered() {
        if (!isOrdered())
            throw new IllegalStateException("Cannot call on unordered element");
    }

    public boolean put(IElement key, IElement value) {
        IElement oldElement = getMemory().put(key, value);
        return oldElement != null;
    }

    private LinkedHashMap<IElement, IElement> getMemoryOrdered() {
        assureOrdered();
        return (LinkedHashMap<IElement, IElement>) getMemory();
    }

    public boolean contains(IElement key) {
        return getMemory().containsKey(key);
    }

    public void clear() {
        getMemory().clear();
    }

    public boolean remove(IElement key) {
        final IElement removedValue = getMemory().remove(key);
        return removedValue != null;
    }

}
