package com.dcrux.haufen.element.common;

import com.dcrux.haufen.IElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 10/09/14.
 */
// TODO: Make more generic. Should be accessor1/accessor2 not type/accessor - since for the table we need 2 accessors but no type.
public interface ITypedAccessor<TTarget extends IElement> {
    @Nullable
    TTarget access(IElement type /* Rename to accessor 1*/, IElement accessor);
}
