package com.dcrux.haufen.newimpl;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;

/**
 * Created by caelis on 27/08/14.
 */
public interface IElementFactory {
    Type getBaseType();

    IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider);

    IInternalElement create();
}
