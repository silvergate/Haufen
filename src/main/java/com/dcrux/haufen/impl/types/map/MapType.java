package com.dcrux.haufen.impl.types.map;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caelis on 11/08/14.
 */
public class MapType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.map;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        final List<int[]> references = (List<int[]>) payload;
        for (int[] entry : references) {
            Varint.writeUnsignedVarInt(entry[0], dataOutput);
            Varint.writeUnsignedVarInt(entry[1], dataOutput);
        }
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        final List<int[]> entries = new ArrayList<>();
        final long startCount = dataInput.getPosition();
        long numberProcessed = 0;
        while (numberProcessed < length) {
            final int key = Varint.readUnsignedVarInt(dataInput);
            final int value = Varint.readUnsignedVarInt(dataInput);
            entries.add(new int[]{key, value});
            numberProcessed = dataInput.getPosition() - startCount;
        }
        ;
        return entries;
    }
}
