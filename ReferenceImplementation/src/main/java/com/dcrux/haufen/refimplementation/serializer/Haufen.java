package com.dcrux.haufen.refimplementation.serializer;

import com.dcrux.haufen.*;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.refimplementation.IElementCreator;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 08/09/14.
 */
public class Haufen implements IHaufen, IElementCreator {

    private final Serializer serializer = new Serializer();
    private final Deserializer deserializer = new Deserializer(this);
    private final FactoryProvider factoryProvider = new FactoryProvider();

    @Override
    public <T extends IElement> T create(Types<T> type) {
        return (T) this.factoryProvider.get(type.getType()).create(this);
    }

    @Override
    public <T extends IElement> T create(Type type, Class<T> clazz) {
        return (T) this.factoryProvider.get(type).create(this);
    }

    @Override
    public void serialize(IDataOutput dataOutput, IElement element) {
        this.serializer.serialize(dataOutput, (IInternalElement) element);
    }

    @Override
    public IDisposableElement deserialize(IDataInput dataInput) {
        final IInternalElement element = this.deserializer.deserialize(dataInput);
        return new IDisposableElement() {
            @Override
            public IElement get() {
                return element;
            }

            @Override
            public void dispose() {
                throw new UnsupportedOperationException("TODO: Implement me");
            }
        };
    }
}
