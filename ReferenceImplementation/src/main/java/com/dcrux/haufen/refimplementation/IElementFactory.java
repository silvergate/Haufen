package com.dcrux.haufen.refimplementation;

import com.dcrux.haufen.Type;

/**
 * Created by caelis on 27/08/14.
 */
public interface IElementFactory {
    Type getBaseType();

    IInternalElement createUninitialized(IElementCreator elementCreator);

    IInternalElement create(IElementCreator elementCreator);
}
