package com.dcrux.haufen.impl.base;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.types.TypeProvider;
import com.dcrux.haufen.impl.types.index.IIndexEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caelis on 23/07/14.
 */
public class Serializer implements IStreamSerializer {

    private IDataOutput dataOutput;
    private List<Element> elements = new ArrayList<>();
    private boolean headerWritten;

    public void setOutput(IDataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }

    @Override
    public void write(BaseType baseType, byte subtype, Object payload) throws Exception {
        if (!this.headerWritten) {
            /* Write header */
            writeHeader();
            this.headerWritten = true;
        }
        /* Get the type */
        IStreamType typeImplementation = getType(baseType);
        long currentCount = this.dataOutput.getPosition();
        typeImplementation.serialize(getDataOutput(), subtype, payload);
        long len = this.dataOutput.getPosition() - currentCount;

        /* Write element */
        Element element = new Element(baseType, subtype, false, len);
        this.elements.add(element);
    }

    @Override
    public void done() throws Exception {
        final IStreamType type = getType(BaseType.index);
        long currentCount = this.dataOutput.getPosition();
        type.serialize(getDataOutput(), (byte) 0, this.elements);
        long len = this.dataOutput.getPosition() - currentCount;

        /* Write the length of the index at the end. Inverse! */
        byte[] indexLenBytes = Varint.writeUnsignedVarInt((int) len);
        BinaryUtil.reverse(indexLenBytes);

        getDataOutput().write(indexLenBytes);
    }

    @Deprecated
    private void writeHeader() {
        //TODO
    }

    private IDataOutput getDataOutput() {
        return this.dataOutput;
    }

    private IStreamType getType(BaseType baseType) {
        return (new TypeProvider()).getType(baseType);
    }

    class Element implements IIndexEntry {
        private final BaseType baseType;
        private final byte subtype;
        private final boolean followingAnnotation;
        private final long length;

        Element(BaseType baseType, byte subtype, boolean followingAnnotation, long length) {
            this.baseType = baseType;
            this.subtype = subtype;
            this.followingAnnotation = followingAnnotation;
            this.length = length;
        }

        @Override
        public BaseType getBaseType() {
            return this.baseType;
        }

        @Override
        public byte getSubtype() {
            return this.subtype;
        }

        @Override
        public boolean hasFollowingAnnotation() {
            return this.followingAnnotation;
        }

        @Override
        public long getLength() {
            return this.length;
        }
    }
}
