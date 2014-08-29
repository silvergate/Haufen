package com.dcrux.haufen.impl.base;

import com.dcrux.haufen.impl.common.BaseType;

/**
 * Created by caelis on 03/08/14.
 */

public interface IStreamType {

    BaseType getBaseType();

    void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception;

    Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception;
}
