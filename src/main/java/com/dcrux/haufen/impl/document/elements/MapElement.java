package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.*;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.*;

/**
 * Created by caelis on 11/08/14.
 */
public class MapElement extends AbstractElement {

    private static final IDocumentElementFactory factory = new IDocumentElementFactory() {
        @Override
        public BaseType getBaseType() {
            return BaseType.string;
        }

        @Override
        public ISecondStageDocumentElementFactory create(Object payload) {
            return new MapElement((List<int[]>) payload);
        }
    };
    private final Map<IDocumentElement, IDocumentElement> map = new TreeMap<IDocumentElement, IDocumentElement>((o1, o2) -> o1.getCanonicalComparator().compare(o1, o2));
    private List<int[]> intRefs;

    public MapElement() {
        this.intRefs = null;
    }

    private MapElement(List<int[]> intRefs) {
        super(false);
        this.intRefs = intRefs;
    }

    public static IDocumentElementFactory getFactory() {
        return factory;
    }

    public IDocumentElement put(IDocumentElement key, IDocumentElement value) {
        assert (key != null);
        assert (value != null);
        return this.map.put(key, value);
    }

    public IDocumentElement get(IDocumentElement key) {
        return this.map.get(key);
    }

    @Override
    protected Comparator<IDocumentElement> getSameTypeCanonicalComparator() {
        //TODO
        return null;
    }

    @Override
    protected void initializeInternal(IInverseReferenceMap inverseReferenceMap) {
        for (int[] element : this.intRefs) {
            final IDocumentElement key = inverseReferenceMap.getElement(element[0]);
            final IDocumentElement value = inverseReferenceMap.getElement(element[1]);
            this.map.put(key, value);
        }
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.map;
    }

    @Override
    public void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception {
        List<int[]> refs = new ArrayList<>();
        for (Map.Entry<IDocumentElement, IDocumentElement> entry : this.map.entrySet()) {
            final int key = referenceMap.getReference(entry.getKey());
            final int value = referenceMap.getReference(entry.getValue());
            refs.add(new int[]{key, value});
        }
        serializer.write(getBaseType(), getSubtype(), refs);
    }

    @Override
    public Set<IDocumentElement> getDependencies() {
        Set<IDocumentElement> deps = new HashSet<>();
        deps.addAll(this.map.keySet());
        deps.addAll(this.map.values());
        return deps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapElement that = (MapElement) o;

        if (!map.equals(that.map)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mapHashCode(this.map);
    }

    public int mapHashCode(Map<IDocumentElement, IDocumentElement> map) {
        int h = 0;
        Iterator<Map.Entry<IDocumentElement, IDocumentElement>> i = map.entrySet().iterator();
        while (i.hasNext())
            h += elementHashCode(i.next());
        return h;
    }

    public int elementHashCode(Map.Entry<IDocumentElement, IDocumentElement> entry) {
        IDocumentElement key = entry.getKey();
        if (key == this)
            key = null;
        IDocumentElement value = entry.getValue();
        if (value == this)
            value = null;
        int keyHash = (key == null ? 0 : key.hashCode());
        int valueHash = (value == null ? 0 : value.hashCode());
        return keyHash ^ valueHash;
    }

    @Override
    public String toString() {
        return "MapElement{" +
                mapToString(this.map) +
                '}';
    }

    public String mapToString(Map<IDocumentElement, IDocumentElement> map) {
        Iterator<Map.Entry<IDocumentElement, IDocumentElement>> i = map.entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            Map.Entry<IDocumentElement, IDocumentElement> e = i.next();
            IDocumentElement key = e.getKey();
            IDocumentElement value = e.getValue();
            sb.append(key == this ? "[this]" : key);
            sb.append('=');
            sb.append(value == this ? "[this]" : value);
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }
}
