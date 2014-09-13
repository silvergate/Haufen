package com.dcrux.haufen.refimplementation.element.map;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.common.IElementPair;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.map.IMapEntry;
import com.dcrux.haufen.element.map.IOrderedMapElement;
import com.dcrux.haufen.refimplementation.IElementCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class OrderedMapElement extends MapElement implements IOrderedMapElement {

    private static final int ACCESS_TYPE_VALUE_BY_NUMBER = 0;
    private static final int ACCESS_TYPE_KEY_BY_NUMBER = 1;
    private final IElementCreator elementCreator;

    public OrderedMapElement(boolean initialized, IElementCreator elementCreator) {
        super(initialized, elementCreator);
        this.elementCreator = elementCreator;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }

    @Nullable
    @Override
    public IElement access(IElement type, IElement accessor) {
        if (type.is(Type.empty)) {
            return super.access(type, accessor);
        } else {
            if (type.is(Type.integer)) {
                final IIntegerElement integer = type.as(Types.INTEGER);
                switch ((int) integer.get()) {
                    case ACCESS_TYPE_KEY_BY_NUMBER:
                        return accessKeyByNumber(accessor);
                    case ACCESS_TYPE_VALUE_BY_NUMBER:
                        return accessValueByNumber(accessor);
                }
            }
            return null;
        }
    }

    @Nullable
    private IElement accessKeyByNumber(IElement accessor) {
        IMapEntry<IElement, IElement> element = accessByNumber(accessor);
        return (element != null) ? element.getKey() : null;
    }

    @Nullable
    private IElement accessValueByNumber(IElement accessor) {
        IMapEntry<IElement, IElement> element = accessByNumber(accessor);
        return (element != null) ? element.getValue() : null;
    }

    @Nullable
    public IMapEntry<IElement, IElement> accessByNumber(IElement accessor) {
        final long number;
        if (accessor.is(Type.integer)) {
            number = accessor.as(Types.INTEGER).get();
        } else {
            return null;
        }
        final Iterator<IMapEntry<IElement, IElement>> iterator = iterator();
        long i = 0;
        while (true) {
            if (!iterator.hasNext())
                return null;
            IMapEntry<IElement, IElement> element = iterator.next();
            if (i == number)
                return element;
            i++;
        }
    }

    @Override
    public IElementPair<IElement, IElement> getAccessorForValue(int index) {
        final IElement first = this.elementCreator.create(Types.INTEGER).set(ACCESS_TYPE_VALUE_BY_NUMBER);
        final IElement second = this.elementCreator.create(Types.INTEGER).set(index);
        return new IElementPair<IElement, IElement>() {
            @Override
            public IElement getFirst() {
                return first;
            }

            @Override
            public IElement getSecond() {
                return second;
            }
        };
    }

    @Override
    public IElementPair<IElement, IElement> getAccessorForKey(int index) {
        final IElement first = this.elementCreator.create(Types.INTEGER).set(ACCESS_TYPE_KEY_BY_NUMBER);
        final IElement second = this.elementCreator.create(Types.INTEGER).set(index);
        return new IElementPair<IElement, IElement>() {
            @Override
            public IElement getFirst() {
                return first;
            }

            @Override
            public IElement getSecond() {
                return second;
            }
        };
    }
}
