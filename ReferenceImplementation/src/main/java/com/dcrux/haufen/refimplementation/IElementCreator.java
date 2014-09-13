package com.dcrux.haufen.refimplementation;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Types;

/**
 * Created by caelis on 10/09/14.
 */
public interface IElementCreator {
    <T extends IElement> T create(Types<T> type);
}
