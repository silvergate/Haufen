package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;

/**
 * Created by caelis on 13/09/14.
 */
public interface ISingleCheckFactory<T extends ICheck> {
    T create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IElement element);

    String getTypeIdentifier();
}
