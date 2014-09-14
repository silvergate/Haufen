package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.element.map.IMapElement;

/**
 * Created by caelis on 13/09/14.
 */
public interface ISingleCheckFactory<T extends ICheck> {
    T create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IMapElement configElement);

    String getTypeIdentifier();
}
