package com.dcrux.haufen.impl;

import com.dcrux.haufen.impl.common.BaseType;

/**
 * Created by caelis on 20/08/14.
 */
public class Head {
    private byte subtype;
    private BaseType baseType;
    private boolean followingAnnotation;

    public byte getSubtype() {
        return subtype;
    }

    public void setSubtype(byte subtype) {
        if (subtype > 3 || subtype < 0)
            throw new IllegalArgumentException("Subtype valid 0-3 (inclusive).");
        this.subtype = subtype;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public boolean isFollowingAnnotation() {
        return followingAnnotation;
    }

    public void setFollowingAnnotation(boolean followingAnnotation) {
        this.followingAnnotation = followingAnnotation;
    }
}
