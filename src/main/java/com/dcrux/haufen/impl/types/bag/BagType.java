package com.dcrux.haufen.impl.types.bag;

import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.base.IStreamType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caelis on 10/08/14.
 */
//TODO: Für die Grössenoptimierung: Ab 4 gleichen elementen einen Spezialeintrag machen (zusammenfassung) -> kann ein eigener typ sein.
public class BagType implements IStreamType {
    @Override
    public BaseType getBaseType() {
        return BaseType.bag;
    }

    @Override
    public void serialize(IDataOutput dataOutput, byte subtype, Object payload) throws Exception {
        final List<Integer> references = (List<Integer>) payload;
        for (Integer reference : references) {
            Varint.writeUnsignedVarInt(reference, dataOutput);
        }
    }

    @Override
    public Object deserialize(IDataInput dataInput, byte subtype, long length) throws Exception {
        final List<Integer> entries = new ArrayList<>();
        final long startCount = dataInput.getPosition();
        long numberProcessed = 0;
        while (numberProcessed < length) {
            final int reference = Varint.readUnsignedVarInt(dataInput);
            entries.add(reference);
            numberProcessed = dataInput.getPosition() - startCount;
        }
        return entries;
    }
}
