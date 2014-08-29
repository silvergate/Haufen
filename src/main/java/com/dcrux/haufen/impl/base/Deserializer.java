package com.dcrux.haufen.impl.base;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.types.TypeProvider;
import com.dcrux.haufen.impl.types.index.IIndexEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caelis on 25/07/14.
 */
public class Deserializer implements IStreamDeserializer {
    private IDataInput dataInput;
    private List<IIndexEntry> entries;
    private int currentEntry;

    private IDataInput getDataInput() {
        return this.dataInput;
    }

    public void setDataInput(IDataInput dataInput) {
        this.dataInput = dataInput;
    }

    public void readHeader(int length, IDataInput headerDataInput) throws Exception {
        if (this.entries != null)
            throw new IllegalStateException("Header has already been read.");
        final IStreamType type = getType(BaseType.index);
        this.entries = (List<IIndexEntry>) type.deserialize(headerDataInput, (byte) 0, length);
    }

    private void assureHeaderRead() throws Exception {
        if (this.entries != null)
            return;
        final long length = getDataInput().getLength();
        final long position = length - 5;
        getDataInput().seek(position);
        final byte[] indexLength = new byte[5];
        getDataInput().readFully(indexLength);
        /* Inverse */
        BinaryUtil.reverse(indexLength);
        final int indexLengthLength = Varint.readUnsignedVarIntCount(indexLength);
        final int indexLengthAsInt = Varint.readUnsignedVarInt(indexLength);

        /* Set position for the header */
        long lastPosition = getDataInput().getLength() - 1;
        getDataInput().seek(lastPosition - indexLengthLength - indexLengthAsInt + 1);

        /* Read the header */
        readHeader(indexLengthAsInt, getDataInput());

        /* Go back to first position */
        getDataInput().seek(0);
    }

    @Override
    public Iterator<IEntryHeader> getNewHeaderIterator() throws Exception {
        //TODO: Make streamable to not to keep this in memory
        assureHeaderRead();
        List<IEntryHeader> entryHeaders = new ArrayList<>();
        for (IIndexEntry entry : this.entries) {
            entryHeaders.add(new IEntryHeader() {
                @Override
                public BaseType getBaseType() {
                    return entry.getBaseType();
                }

                @Override
                public byte getSubtype() {
                    return entry.getSubtype();
                }
            });
        }
        return entryHeaders.iterator();
    }

    public boolean readEntry(IEntry outEntry) throws Exception {
        assureHeaderRead();
        if (this.entries == null || this.entries.size() <= this.currentEntry) {
            return false;
        }
        final IIndexEntry entry = this.entries.get(this.currentEntry);
        final IStreamType typeImplementation = getType(entry.getBaseType());
        final Object payload = typeImplementation.deserialize(getDataInput(), entry.getSubtype(), entry.getLength());
        this.currentEntry++;
        outEntry.setPayload(payload);
        outEntry.setBaseType(entry.getBaseType());
        outEntry.setSubtype(entry.getSubtype());
        return true;
    }

    private IStreamType getType(BaseType baseType) {
        return (new TypeProvider()).getType(baseType);
    }
}
