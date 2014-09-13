package com.dcrux.haufen;


import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;

/**
 * Created by caelis on 19/08/14.
 */
public interface IHaufen {
    <T extends IElement> T create(Types<T> type);

    <T extends IElement> T create(Type type, Class<T> clazz);

    void serialize(IDataOutput dataOutput, IElement element);

    IDisposableElement deserialize(IDataInput dataInput);
}
