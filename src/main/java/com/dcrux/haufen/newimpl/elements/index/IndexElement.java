package com.dcrux.haufen.newimpl.elements.index;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Head;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BaseCanonicalComparatorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 01/09/14.
 */
public class IndexElement implements IElement {

    private List<IndexEntry> entries = new ArrayList<>();

    public List<IndexEntry> getEntries() {
        return entries;
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.index;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        long beginPosition = output.getPosition();
        for (IndexEntry entry : this.entries) {
            write(output, entry);
        }
        long endPosition = output.getPosition();
        /* Write length at the end */
        output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarLong(endPosition - beginPosition)));
    }

    private void write(IDataOutput output, IndexEntry entry) {
        Head head = new Head();
        head.setBaseType(entry.getBaseType());
        head.setSubtype(entry.getSubtype());
        BinaryUtil.writeHead(output, head);
        Varint.writeUnsignedVarLong(entry.getLength(), output);
    }

    @Override
    public Iterator<IElement> getDependencies() {
        return Collections.<IElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return this.entries.size() * 3;
    }

    @Override
    public int canonicalCompareTo(IElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        throw new UnsupportedOperationException("Canonical compare is not supported for this type");
    }

    @Override
    public boolean isClosed() {
        return this.entries == null;
    }

    @Override
    public void close() throws Exception {
        this.entries = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexElement that = (IndexElement) o;

        if (!entries.equals(that.entries)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "IndexElement{" +
                "entries=" + entries +
                '}';
    }
}
