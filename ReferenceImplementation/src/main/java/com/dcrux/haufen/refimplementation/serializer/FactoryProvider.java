package com.dcrux.haufen.refimplementation.serializer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.element.annotated.AnnotatedElementFactory;
import com.dcrux.haufen.refimplementation.element.annotation.AnnotationElementFactory;
import com.dcrux.haufen.refimplementation.element.bag.BagElementFactory;
import com.dcrux.haufen.refimplementation.element.binary.BinarayElementFactory;
import com.dcrux.haufen.refimplementation.element.bool.BoolElementFactory;
import com.dcrux.haufen.refimplementation.element.empty.EmptyElementFactory;
import com.dcrux.haufen.refimplementation.element.index.IndexElementFactory;
import com.dcrux.haufen.refimplementation.element.integer.IntegerElementFactory;
import com.dcrux.haufen.refimplementation.element.list.ListElementFactory;
import com.dcrux.haufen.refimplementation.element.map.MapElementFactory;
import com.dcrux.haufen.refimplementation.element.map.OrderedMapElementFactory;
import com.dcrux.haufen.refimplementation.element.number.NumberElementFactory;
import com.dcrux.haufen.refimplementation.element.set.OrderedSetElementFactory;
import com.dcrux.haufen.refimplementation.element.set.SetElementFactory;
import com.dcrux.haufen.refimplementation.element.string.StringElementFactory;

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
        this.factories.put(Type.list, new ListElementFactory());
        this.factories.put(Type.set, new SetElementFactory());
        this.factories.put(Type.orderedSet, new OrderedSetElementFactory());
        this.factories.put(Type.binary, new BinarayElementFactory());
        this.factories.put(Type.annotation, new AnnotationElementFactory());
        this.factories.put(Type.annotated, new AnnotatedElementFactory());
        this.factories.put(Type.number, new NumberElementFactory());
        this.factories.put(Type.empty, new EmptyElementFactory());
    }

    public IElementFactory get(Type type) {
        return this.factories.get(type);
    }
}
