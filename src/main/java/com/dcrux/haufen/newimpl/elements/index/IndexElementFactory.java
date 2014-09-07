package com.dcrux.haufen.newimpl.elements.index;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Head;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;

/**
 * Created by caelis on 01/09/14.
 */
public class IndexElementFactory implements IElementFactory {
    @Override
    public BaseType getBaseType() {
        return BaseType.index;
    }

    @Override
    public IElement create(IDataInput data, byte subtype, IElementProvider elementProvider) {
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
        IndexEntry indexEntry = new IndexEntry(head.getBaseType(), head.getSubtype(), currentIndex, length);
        return indexEntry;
    }

    @Override
    public IElement create() {
        return new IndexElement();
    }
}
