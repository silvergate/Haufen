package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 13/09/14.
 */
public interface ICheckFactory {
    ICheck create(IElement element);
}
