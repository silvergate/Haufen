package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.common.IElementPair;
import com.dcrux.haufen.element.map.IBaseMapElement;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.map.IMapEntry;
import com.dcrux.haufen.element.map.IMapKeys;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.element.BoxedValue;
import com.dcrux.haufen.refimplementation.utils.BinaryUtil;
import com.dcrux.haufen.refimplementation.utils.ElementCastUtil;
import com.dcrux.haufen.refimplementation.utils.InverseDataInput;
import com.dcrux.haufen.refimplementation.utils.Varint;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Created by caelis on 01/09/14.
 */
public class MapElement extends BaseElement implements IInternalElement, IMapElement {

    private static final int NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER = 32;
    private final IMapKeys mapKeys = new IMapKeys() {
        private final MapElement outer = MapElement.this;

        @Override
        public IMapKeys clear() {
            outer.clear();
            return this;
        }

        @Override
        public Iterator<IElement> iterator() {
            final Iterator<IMapEntry<IElement, IElement>> original = outer.iterator();
            return new Iterator<IElement>() {
                @Override
                public boolean hasNext() {
                    return original.hasNext();
                }

                @Override
                public IElement next() {
                    return original.next().getKey();
                }
            };
        }

        @Override
        public boolean isEmpty() {
            return outer.isEmpty();
        }

        @Override
        public int getNumberOfEntries() {
            return outer.getNumberOfEntries();
        }

        @Override
        public boolean remove(IElement element) {
            return outer.remove(ElementCastUtil.getInstance().cast(element));
        }

        @Override
        public IMapKeys add(IElement element) {
            throw new UnsupportedOperationException("Add is not supported on the key set of a map");
        }

        @Override
        public boolean contains(IElement element) {
            return outer.contains(ElementCastUtil.getInstance().cast(element));
        }

        @Override
        public int getCount(IElement element) {
            if (outer.contains(ElementCastUtil.getInstance().cast(element)))
                return 1;
            else
                return 0;
        }
    };
    private Map<IInternalElement, IInternalElement> memory;
    private int numberOfElements;
    private IElementCreator elementCreator;

    public MapElement(boolean initialized, IElementCreator elementCreator) {
        this.elementCreator = elementCreator;
        if (initialized) {
            this.memory = createMemoryMap();
            this.numberOfElements = 0;
        }
    }

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        final boolean hasAdditionalHeader = hasAdditionalHeader(subtype);
        final MapHeader mapHeader = readBagHeaderFromData(data, hasAdditionalHeader);
        final Map<IInternalElement, IInternalElement> memory = fromHeaderToMap(mapHeader, elementProvider, this::createMemoryMap);
        this.numberOfElements = mapHeader.getNumberOfElements();
        this.memory = memory;
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
        if (getNumberOfEntries() > NUMBER_OF_ELEMENTS_FOR_ADDITIONAL_HEADER)
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

    private String elementToString(IElement element) {
        if (element == this)
            return "[THIS]";
        else
            return element.toString();
    }

    private String memoryToString() {
        StringBuilder stringBuilder = new StringBuilder();
        final boolean[] firstElement = {true};
        getMemory().entrySet().forEach(entry -> {
            if (firstElement[0]) {
                firstElement[0] = false;
            } else {
                stringBuilder.append(",");
            }
            stringBuilder.append(elementToString(entry.getKey()));
            stringBuilder.append("=");
            stringBuilder.append(elementToString(entry.getValue()));
        });
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        if (isOrdered()) {
            return "OrderedMapElement{" +
                    memoryToString() +
                    '}';
        } else {
            return "MapElement{" +
                    memoryToString() +
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

    public <K, V> int hashCodeForEntry(Map.Entry<K, V> entry) {
        int codeForKey = entry.getKey() == this ? 0 : (entry.getKey() == null ? 0 : entry.getKey().hashCode());
        int codeForValue = entry.getValue() == this ? 0 : (entry.getValue() == null ? 0 : entry.getValue().hashCode());
        return codeForKey ^
                codeForValue;
    }

    private <K, V> int hashCodeForMap(Map<K, V> map) {
        int h = 0;
        Iterator<Map.Entry<K, V>> i = map.entrySet().iterator();
        while (i.hasNext()) {
            h += hashCodeForEntry(i.next());
        }
        return h;
    }

    @Override
    public int hashCode() {
        return hashCodeForMap(memory);
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

    @Nullable
    public int getNumberOfEntries() {
        return numberOfElements;
    }

    public boolean remove(IInternalElement key) {
        final IInternalElement removedValue = getMemory().remove(key);
        return removedValue != null;
    }

    @Override
    public boolean remove(IMapEntry<IElement, IElement> elementIElementIMapEntry) {
        return getMemory().remove(ElementCastUtil.getInstance().cast(elementIElementIMapEntry.getKey()), ElementCastUtil.getInstance().cast(elementIElementIMapEntry.getValue()));
    }

    @Override
    public IBaseMapElement add(IMapEntry<IElement, IElement> iElementIElementIMapEntry) {
        return put(iElementIElementIMapEntry.getKey(), iElementIElementIMapEntry.getValue());
    }

    @Override
    public boolean contains(IMapEntry<IElement, IElement> iElementIElementIMapEntry) {
        final IElement value = getMemory().get(ElementCastUtil.getInstance().cast(iElementIElementIMapEntry.getKey()));
        return (value != null && value.equals(iElementIElementIMapEntry.getValue()));
    }

    @Override
    public int getCount(IMapEntry<IElement, IElement> iElementIElementIMapEntry) {
        return 0;
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

    private boolean putInternal(IInternalElement key, IInternalElement value) {
        IInternalElement oldElement = getMemory().put(key, value);
        return oldElement != null;
    }

    @Override
    public IMapElement put(IElement key, IElement value) {
        putInternal((IInternalElement) key, (IInternalElement) value);
        return this;
    }

    @Override
    public boolean putInfo(IElement key, IElement value) {
        return putInternal((IInternalElement) key, (IInternalElement) value);
    }

    @Nullable
    @Override
    public IElement get(IElement key) {
        return getMemory().get(ElementCastUtil.getInstance().cast(key));
    }

    @Override
    public boolean exists(IElement key) {
        return getMemory().containsKey(ElementCastUtil.getInstance().cast(key));
    }

    private LinkedHashMap<IInternalElement, IInternalElement> getMemoryOrdered() {
        assureOrdered();
        return (LinkedHashMap<IInternalElement, IInternalElement>) getMemory();
    }

    public boolean contains(IInternalElement key) {
        return getMemory().containsKey(key);
    }

    public MapElement clear() {
        getMemory().clear();
        return this;
    }

    @Override
    public Iterator<IMapEntry<IElement, IElement>> iterator() {
        final Iterator<Map.Entry<IInternalElement, IInternalElement>> original = getMemory().entrySet().iterator();
        return new Iterator<IMapEntry<IElement, IElement>>() {
            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public IMapEntry<IElement, IElement> next() {
                final Map.Entry<IInternalElement, IInternalElement> nextElement = original.next();
                return new IMapEntry<IElement, IElement>() {
                    @Override
                    public IElement getKey() {
                        return nextElement.getKey();
                    }

                    @Override
                    public IElement getValue() {
                        return nextElement.getValue();
                    }
                };
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return getMemory().isEmpty();
    }

    @Override
    public IMapKeys getKeys() {
        return this.mapKeys;
    }

    @Nullable
    @Override
    public IElement access(IElement type, IElement accessor) {
        if (type.is(Type.empty)) {
            return getMemory().get(ElementCastUtil.getInstance().cast(accessor));
        } else
            return null;
    }

    @Override
    public IElementPair<IElement, IElement> getAccessorForValue(final IElement key) {
        final IElement first = this.elementCreator.create(Types.EMPTY);
        return new IElementPair<IElement, IElement>() {
            @Override
            public IElement getFirst() {
                return first;
            }

            @Override
            public IElement getSecond() {
                return key;
            }
        };
    }
}
