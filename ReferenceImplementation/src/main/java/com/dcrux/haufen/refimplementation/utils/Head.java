package com.dcrux.haufen.refimplementation.utils;

import com.dcrux.haufen.Type;

/**
 * Created by caelis on 20/08/14.
 */
public class Head {
    private byte subtype;
    private Type type;
    private boolean followingAnnotation;

    public byte getSubtype() {
        return subtype;
    }

    public void setSubtype(byte subtype) {
        if (subtype > 3 || subtype < 0)
            throw new IllegalArgumentException("Subtype valid 0-3 (inclusive).");
        this.subtype = subtype;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isFollowingAnnotation() {
        return followingAnnotation;
    }

    public void setFollowingAnnotation(boolean followingAnnotation) {
        this.followingAnnotation = followingAnnotation;
    }
}
