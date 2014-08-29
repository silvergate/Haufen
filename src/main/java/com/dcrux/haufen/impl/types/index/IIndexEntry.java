package com.dcrux.haufen.impl.types.index;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.common.Type;

/**
 * Created by caelis on 10/08/14.
 */
public interface IIndexEntry {
    BaseType getBaseType();
    byte getSubtype();
    @Deprecated
    boolean hasFollowingAnnotation();
    long getLength();
}
