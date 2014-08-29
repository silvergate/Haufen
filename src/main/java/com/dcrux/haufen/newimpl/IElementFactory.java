package com.dcrux.haufen.newimpl;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;

/**
 * Created by caelis on 27/08/14.
 */
public interface IElementFactory {
    BaseType getBaseType();
    IElement create(IDataInput data, byte subtype, IElementProvider elementProvider);
    IElement create();
}
