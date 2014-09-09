package com.dcrux.haufen.element.map;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.element.common.ICollection;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 08/09/14.
 */
public interface IBaseMapElement extends IElement, ICollection<IMapEntry<IElement, IElement>, IBaseMapElement> {
    IBaseMapElement put(IElement key, IElement value);

    boolean putInfo(IElement key, IElement value);

    @Nullable
    IElement get(IElement key);
}
