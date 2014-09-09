package com.dcrux.haufen.refimplementation.element.index;

import com.dcrux.haufen.Type;

/**
 * Created by caelis on 01/09/14.
 */
public class IndexEntry {
    private Type type;
    private byte subtype;
    private long startIndex;
    private long length;

    public IndexEntry(Type type, byte subtype, long startIndex, long length) {
        this.type = type;
        setSubtype(subtype);
        this.startIndex = startIndex;
        this.length = length;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public byte getSubtype() {
        return subtype;
    }

    public void setSubtype(byte subtype) {
        if (subtype > 3 || subtype < 0)
            throw new IllegalArgumentException("Subtype valid 0-3 (inclusive).");
        this.subtype = subtype;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexEntry that = (IndexEntry) o;

        if (length != that.length) return false;
        if (startIndex != that.startIndex) return false;
        if (subtype != that.subtype) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (int) subtype;
        result = 31 * result + (int) (startIndex ^ (startIndex >>> 32));
        result = 31 * result + (int) (length ^ (length >>> 32));
        return result;
    }
}
