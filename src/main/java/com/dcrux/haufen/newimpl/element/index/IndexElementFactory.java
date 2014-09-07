package com.dcrux.haufen.newimpl.element.index;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.utils.BinaryUtil;
import com.dcrux.haufen.newimpl.utils.Head;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;
import com.dcrux.haufen.newimpl.utils.Varint;

/**
 * Created by caelis on 01/09/14.
 */
public class IndexElementFactory implements IElementFactory {
    @Override
    public Type getBaseType() {
        return Type.index;
    }

    @Override
    public IInternalElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
        InverseDataInput inverseDataInput = new InverseDataInput(data);
        inverseDataInput.seek(0);
        long payloadLength = Varint.readUnsignedVarLong(inverseDataInput);
        /* Set the position */
        final long lenLen = inverseDataInput.getPosition();
        inverseDataInput.seek(payloadLength + lenLen - 1);
        inverseDataInput.close();

        IndexElement indexElement = new IndexElement();
        long currentIndex = 0;
        while (data.getPosition() < data.getLength() - lenLen) {
            IndexEntry entry = read(data, currentIndex);
            currentIndex += entry.getLength();
            indexElement.getEntries().add(entry);
        }
        return indexElement;
    }

    private IndexEntry read(IDataInput input, long currentIndex) {
        Head head = new Head();
        BinaryUtil.readHead(input, head);
        long length = Varint.readUnsignedVarLong(input);
        IndexEntry indexEntry = new IndexEntry(head.getType(), head.getSubtype(), currentIndex, length);
        return indexEntry;
    }

    @Override
    public IInternalElement create() {
        return new IndexElement();
    }
}
