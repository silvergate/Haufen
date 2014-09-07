package com.dcrux.haufen.newimpl.serializer;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.elements.array.ArrayElementFactory;
import com.dcrux.haufen.newimpl.elements.bag.BagElementFactory;
import com.dcrux.haufen.newimpl.elements.binary.BinarayElementFactory;
import com.dcrux.haufen.newimpl.elements.bool.BoolElementFactory;
import com.dcrux.haufen.newimpl.elements.index.IndexElementFactory;
import com.dcrux.haufen.newimpl.elements.integer.IntegerElementFactory;
import com.dcrux.haufen.newimpl.elements.map.MapElementFactory;
import com.dcrux.haufen.newimpl.elements.map.OrderedMapElementFactory;
import com.dcrux.haufen.newimpl.elements.orderedSet.OrderedSetElementFactory;
import com.dcrux.haufen.newimpl.elements.set.SetElementFactory;
import com.dcrux.haufen.newimpl.elements.string.StringElementFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caelis on 01/09/14.
 */
public class FactoryProvider {
    private Map<BaseType, IElementFactory> factories = new HashMap<>();

    public FactoryProvider() {
        add();
    }

    private void add() {
        this.factories.put(BaseType.string, new StringElementFactory());
        this.factories.put(BaseType.integer, new IntegerElementFactory());
        this.factories.put(BaseType.index, new IndexElementFactory());
        this.factories.put(BaseType.bag, new BagElementFactory());
        this.factories.put(BaseType.map, new MapElementFactory());
        this.factories.put(BaseType.orderedMap, new OrderedMapElementFactory());
        this.factories.put(BaseType.bool, new BoolElementFactory());
        this.factories.put(BaseType.array, new ArrayElementFactory());
        this.factories.put(BaseType.set, new SetElementFactory());
        this.factories.put(BaseType.orderedSet, new OrderedSetElementFactory());
        this.factories.put(BaseType.binary, new BinarayElementFactory());
    }

    public IElementFactory get(BaseType type) {
        return this.factories.get(type);
    }
}
