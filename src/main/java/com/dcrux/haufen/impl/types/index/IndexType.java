package com.dcrux.haufen.impl.types.index;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Head;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caelis on 28/07/14.
 */
//TODO: Muss was geben, einzelne einträge zu "packen" (für listen mit vielen einträgen)
//TODO: Packen: Falls mehr als 2 aufenanderfolgende einträge wo bei allen typ UND length identisch ist.
//TODO: Packen: Falls mehr als 4 aufenanderfolgende einträge wo bei allen typ identisch ist.
public class IndexType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.index;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        List<IIndexEntry> entries = (List<IIndexEntry>) payload;
        Head head = new Head();
        for (IIndexEntry entry : entries) {
            head.setBaseType(entry.getBaseType());
            head.setSubtype(entry.getSubtype());
            head.setFollowingAnnotation(entry.hasFollowingAnnotation());
            BinaryUtil.writeHead(dataOutput, head);
            Varint.writeUnsignedVarLong(entry.getLength(), dataOutput);
        }
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        final List<IIndexEntry> entries = new ArrayList<IIndexEntry>();
        final long startCount = dataInput.getPosition();
        long numberProcessed = 0;
        Head head = new Head();
        while (numberProcessed < length) {
            BinaryUtil.readHead(dataInput, head);
            final long entryLength = Varint.readUnsignedVarLong(dataInput);
            final BaseType baseType = head.getBaseType();
            final byte subtypeX = head.getSubtype();
            final boolean flollowingAnnotation = head.isFollowingAnnotation();
            entries.add(new IIndexEntry() {
                @Override
                public BaseType getBaseType() {
                    return baseType;
                }

                @Override
                public byte getSubtype() {
                    return subtypeX;
                }

                @Override
                public boolean hasFollowingAnnotation() {
                    return flollowingAnnotation;
                }

                @Override
                public long getLength() {
                    return entryLength;
                }
            });
            numberProcessed = dataInput.getPosition() - startCount;
        }
        ;
        return entries;
    }
}
