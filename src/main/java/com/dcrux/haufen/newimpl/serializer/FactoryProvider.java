package com.dcrux.haufen.newimpl.serializer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.element.array.ArrayElementFactory;
import com.dcrux.haufen.newimpl.element.bag.BagElementFactory;
import com.dcrux.haufen.newimpl.element.binary.BinarayElementFactory;
import com.dcrux.haufen.newimpl.element.bool.BoolElementFactory;
import com.dcrux.haufen.newimpl.element.index.IndexElementFactory;
import com.dcrux.haufen.newimpl.element.integer.IntegerElementFactory;
import com.dcrux.haufen.newimpl.element.map.MapElementFactory;
import com.dcrux.haufen.newimpl.element.orderedMap.OrderedMapElementFactory;
import com.dcrux.haufen.newimpl.element.orderedSet.OrderedSetElementFactory;
import com.dcrux.haufen.newimpl.element.set.SetElementFactory;
import com.dcrux.haufen.newimpl.element.string.StringElementFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caelis on 01/09/14.
 */
public class FactoryProvider {
    private Map<Type, IElementFactory> factories = new HashMap<>();

    public FactoryProvider() {
        add();
    }

    private void add() {
        this.factories.put(Type.string, new StringElementFactory());
        this.factories.put(Type.integer, new IntegerElementFactory());
        this.factories.put(Type.index, new IndexElementFactory());
        this.factories.put(Type.bag, new BagElementFactory());
        this.factories.put(Type.map, new MapElementFactory());
        this.factories.put(Type.orderedMap, new OrderedMapElementFactory());
        this.factories.put(Type.bool, new BoolElementFactory());
        this.factories.put(Type.array, new ArrayElementFactory());
        this.factories.put(Type.set, new SetElementFactory());
        this.factories.put(Type.orderedSet, new OrderedSetElementFactory());
        this.factories.put(Type.binary, new BinarayElementFactory());
    }

    public IElementFactory get(Type type) {
        return this.factories.get(type);
    }
}
