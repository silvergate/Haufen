package com.dcrux.haufen.element.map;

import com.dcrux.haufen.IElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 08/09/14.
 */
public interface IMapElement extends IElement {
    IMapElement put(IElement key, IElement value);

    boolean putInfo(IElement key, IElement value);

    @Nullable
    IElement get(IElement key);
}
