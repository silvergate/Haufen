package com.dcrux.haufen.newimpl.element.map;

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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Created by caelis on 01/09/14.
 */
public class MapElement extends BaseElement implements IInternalElement {

    private static final int NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER = 32;

    private Map<IInternalElement, IInternalElement> memory;
    private int numberOfElements;

    public MapElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        final boolean hasAdditionalHeader = hasAdditionalHeader(subtype);
        final MapHeader mapHeader = readBagHeaderFromData(dataInput, hasAdditionalHeader);
        final Map<IInternalElement, IInternalElement> memory = fromHeaderToMap(mapHeader, elementProvider, this::createMemoryMap);
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

    private Map<IInternalElement, IInternalElement> createMemoryMap() {
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
            final IInternalElement key = entry.getKey();
            final IInternalElement value = entry.getValue();
            mapHeader.getMap().put(elementIndexProvider.getIndex(key), elementIndexProvider.getIndex(value));
        });

        return mapHeader;
    }

    private void write(IDataOutput output, Map<IInternalElement, IInternalElement> elements, IElementIndexProvider elementIndexProvider,
                       IElementProvider elementProvider, BoxedValue<Boolean> outHasAdditionalHeader) {
        final MapHeader header = getBagHeader(elementIndexProvider, elementProvider);

        /* Write content */
        for (Map.Entry<Integer, Integer> entry : header.getMap().entrySet()) {
            final int key = entry.getKey();
            final int value = entry.getValue();
            Varint.writeUnsignedVarInt(key, output);
            Varint.writeUnsignedVarInt(value, output);
        }

        /* Write the actual header (only if enough element) */
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

    private Map<IInternalElement, IInternalElement> fromHeaderToMap(MapHeader mapHeader, IElementProvider elementProvider,
                                                                    Supplier<Map<IInternalElement, IInternalElement>> createMap) {
        final Map<IInternalElement, IInternalElement> result = createMap.get();
        for (Map.Entry<Integer, Integer> entry : mapHeader.getMap().entrySet()) {
            final int key = entry.getKey();
            final int value = entry.getValue();
            result.put(elementProvider.getElement(key), elementProvider.getElement(value));
        }
        return result;
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        final Iterator<IInternalElement> originalKeys = this.memory.keySet().iterator();
        final Iterator<IInternalElement> originalValues = this.memory.values().iterator();
        return new Iterator<IInternalElement>() {
            @Override
            public boolean hasNext() {
                return originalKeys.hasNext() || originalValues.hasNext();
            }

            @Override
            public IInternalElement next() {
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
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final MapElement that = (MapElement) other;

        Iterator<Map.Entry<IInternalElement, IInternalElement>> thisIterator = getMemory().entrySet().iterator();
        Iterator<Map.Entry<IInternalElement, IInternalElement>> thatIterator = that.getMemory().entrySet().iterator();

        while (thisIterator.hasNext()) {
            final Map.Entry<IInternalElement, IInternalElement> thisEntry = thisIterator.next();
            final Map.Entry<IInternalElement, IInternalElement> thatEntry = thatIterator.next();

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

    private Map<IInternalElement, IInternalElement> getMemory() {
        assert (this.memory != null);
        return this.memory;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Iterator<Map.Entry<IInternalElement, IInternalElement>> getIterator() {
        return this.memory.entrySet().iterator();
    }

    @Override
    public Type getType() {
        if (isOrdered())
            return Type.orderedMap;
        else
            return Type.map;
    }

    @Override
    public byte getSubtype() {
        return createSubtype();
    }

    private void assureOrdered() {
        if (!isOrdered())
            throw new IllegalStateException("Cannot call on unordered element");
    }

    public boolean put(IInternalElement key, IInternalElement value) {
        IInternalElement oldElement = getMemory().put(key, value);
        return oldElement != null;
    }

    private LinkedHashMap<IInternalElement, IInternalElement> getMemoryOrdered() {
        assureOrdered();
        return (LinkedHashMap<IInternalElement, IInternalElement>) getMemory();
    }

    public boolean contains(IInternalElement key) {
        return getMemory().containsKey(key);
    }

    public void clear() {
        getMemory().clear();
    }

    public boolean remove(IInternalElement key) {
        final IInternalElement removedValue = getMemory().remove(key);
        return removedValue != null;
    }

}
