package com.dcrux.haufen.element.common;

import com.dcrux.haufen.IElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 10/09/14.
 */
public interface ITypedAccessor<TTarget extends IElement> {
    @Nullable
    TTarget access(IElement type, IElement accessor);
}
