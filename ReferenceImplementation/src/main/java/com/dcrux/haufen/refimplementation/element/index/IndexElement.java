package com.dcrux.haufen.refimplementation.element.index;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.refimplementation.element.BaseElement;
import com.dcrux.haufen.refimplementation.utils.BinaryUtil;
import com.dcrux.haufen.refimplementation.utils.Head;
import com.dcrux.haufen.refimplementation.utils.InverseDataInput;
import com.dcrux.haufen.refimplementation.utils.Varint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 01/09/14.
 */
public class IndexElement extends BaseElement implements IInternalElement {

    private List<IndexEntry> entries = new ArrayList<>();

    @Override
    public void initialize(IDataInput data, byte subtype, IElementProvider elementProvider) {
        InverseDataInput inverseDataInput = new InverseDataInput(data);
        inverseDataInput.seek(0);
        long payloadLength = Varint.readUnsignedVarLong(inverseDataInput);
        /* Set the position */
        final long lenLen = inverseDataInput.getPosition();
        inverseDataInput.seek(payloadLength + lenLen - 1);
        inverseDataInput.close();

        long currentIndex = 0;
        while (data.getPosition() < data.getLength() - lenLen) {
            IndexEntry entry = read(data, currentIndex);
            currentIndex += entry.getLength();
            getEntries().add(entry);
        }
    }

    private IndexEntry read(IDataInput input, long currentIndex) {
        Head head = new Head();
        BinaryUtil.readHead(input, head);
        long length = Varint.readUnsignedVarLong(input);
        IndexEntry indexEntry = new IndexEntry(head.getType(), head.getSubtype(), currentIndex, length);
        return indexEntry;
    }

    public List<IndexEntry> getEntries() {
        return entries;
    }

    @Override
    public Type getType() {
        return Type.index;
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
        head.setType(entry.getType());
        head.setSubtype(entry.getSubtype());
        BinaryUtil.writeHead(output, head);
        Varint.writeUnsignedVarLong(entry.getLength(), output);
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public long getSizeFootprint() {
        return this.entries.size() * 3;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
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
