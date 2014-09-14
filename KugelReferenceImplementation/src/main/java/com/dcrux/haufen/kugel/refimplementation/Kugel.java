package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.kugel.refimplementation.check.integer.IntegerValueCheck;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caelis on 13/09/14.
 */
public class Kugel implements ICheckFactory {

    private Map<String, ISingleCheckFactory> factories = new HashMap<>();
    private IHaufen haufen;
    private IElement keyType;

    public Kugel(IHaufen haufen) {
        this.haufen = haufen;
        this.keyType = haufen.create(Types.STRING).set("type");
        add(IntegerValueCheck.FACTORY);
    }

    private void add(ISingleCheckFactory factory) {
        this.factories.put(factory.getTypeIdentifier(), factory);
    }

    @Override
    public ICheck create(IElement element) {
        if (!element.is(Type.map))
            throw new IllegalArgumentException("Element is not a map");
        IMapElement map = element.as(Types.MAP);
        IElement typeName = map.get(this.keyType);
        if (typeName == null)
            throw new IllegalArgumentException("Element has no type");
        if (!typeName.is(Type.string))
            throw new IllegalArgumentException("Element type name is not of type 'string'.");
        final String name = typeName.as(Types.STRING).get();
        ISingleCheckFactory factory = this.factories.get(name);
        if (factory == null)
            throw new IllegalArgumentException("Have no factory for type '" + name + "'.");
        return factory.create(this.haufen, this, map);
    }

    public ICheck create(String type) {
        IMapElement element = this.haufen.create(Types.MAP);
        element.put(this.keyType, this.haufen.create(Types.STRING).set(type));
        return this.factories.get(type).create(this.haufen, this, element);
    }
}
