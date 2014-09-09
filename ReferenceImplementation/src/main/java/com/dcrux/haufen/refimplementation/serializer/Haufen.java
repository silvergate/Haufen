package com.dcrux.haufen.refimplementation.serializer;

import com.dcrux.haufen.IDisposableElement;
import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 08/09/14.
 */
public class Haufen implements IHaufen {

    private final Serializer serializer = new Serializer();
    private final Deserializer deserializer = new Deserializer();
    private final FactoryProvider factoryProvider = new FactoryProvider();

    @Override
    public <T extends IElement> T create(Types<T> type) {
        return (T) this.factoryProvider.get(type.getType()).create();
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
