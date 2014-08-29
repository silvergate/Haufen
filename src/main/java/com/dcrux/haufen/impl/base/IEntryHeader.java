package com.dcrux.haufen.impl.base;

import com.dcrux.haufen.impl.common.BaseType;

/**
 * Created by caelis on 10/08/14.
 */
public interface IEntryHeader {
    BaseType getBaseType();
    byte getSubtype();
}
