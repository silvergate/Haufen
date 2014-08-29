package com.dcrux.haufen.newimpl.elements;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by caelis on 27/08/14.
 */
public class StringElement implements IElement {

    private static final int WITH_SIZE_THRESHOLD = 12;

    private String value;
    private IDataInput input;
    private Integer length;

    public StringElement(String value)  {
        this.value = "";
        setValue(value);
    }

    public StringElement(IDataInput data) {
        this.input = data;
    }

    @Override
    public BaseType getBaseType() {
        return BaseType.string;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    @Override
    public void write(IDataOutput output) throws IOException {
        assureNotClosed();
        if (this.value!=null) {
            byte[] stringAsBytes = this.value.getBytes("UTF-8");
            if (stringAsBytes.length>WITH_SIZE_THRESHOLD) {
                Varint.writeUnsignedVarInt(this.value.length(), output);
                output.write(stringAsBytes);
            } else {
                output.write(stringAsBytes);
            }
        } else {
            BinaryUtil.transfer(output, this.input);
        }
    }

    @Override
    public Set<IElement> getDependencies() {
        assureNotClosed();
        return Collections.emptySet();
    }

    public String getValue() throws IOException {
        assureNotClosed();
        if (this.value==null) {
            this.input.seek(0);
            if (this.input.getLength()>WITH_SIZE_THRESHOLD) {
                Varint.readUnsignedVarLong(this.input);
            }
                byte[] stringAsBytes = new byte[(int) this.input.getLength()];
            this.input.readFully(stringAsBytes);
            this.value = new String(stringAsBytes, "UTF-8");
            this.length = this.value.length();
            return this.value;
        } else {
            return this.value;
        }
    }

    public void setValue(String value) {
        assureNotClosed();
        if (value==null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
        this.length = value.length();
        if (this.input!=null) {
            try {
                this.input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.input = null;
        }
    }

    public int getLength() throws IOException {
        assureNotClosed();
        if (this.length!=null) {
            return this.length;
        } else {
            if (this.input.getLength()>WITH_SIZE_THRESHOLD) {
                this.input.seek(0);
                this.length = (int)Varint.readUnsignedVarLong(this.input);
                return this.length;
            } else {
                return getValue().length();
            }
        }
    }

    public Reader getReader() throws IOException {
        assureNotClosed();
        if (this.value!=null) {
            return new StringReader(this.value);
        } else {
            final long seekStart;
            if (this.input.getLength()>WITH_SIZE_THRESHOLD) {
                this.input.seek(0);
                Varint.readUnsignedVarLong(this.input);
                seekStart = this.input.getPosition();
            } else {
                seekStart = 0;
            }

            return new InputStreamReader(new InputStream() {
                private int position;
                @Override
                public int read() throws IOException {
                    StringElement.this.input.seek(seekStart + position);
                    position++;
                    byte aByte = StringElement.this.input.readByte();
                    return Byte.toUnsignedInt(aByte);
                };
            }, "UTF-8");
        }
    }

    @Override
    public long getSizeFootprint() throws IOException {
        assureNotClosed();
        return getLength();
    }

    @Override
    public int canonicalCompareTo(IElement other) throws IOException {
        assureNotClosed();
        int cmp = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (cmp!=0)
            return cmp;
        StringElement otherCast = (StringElement)other;

        //TODO: Compare codepoints.

        /*try (Reader thisReader = getReader();
        Reader otherReader = otherCast.getReader()) {
            do {
              int thisUnicode = thisReader.read();
                int otherUnicode = otherReader.read();
                //if (thisUnicode)

            }

        }*/

        //this.value.chars();

        return 0;
    }

    @Override
    public boolean isClosed() {
        return this.input==null && this.value==null;
    }

    private void assureNotClosed() {
if (isClosed())
    throw new IllegalStateException("Element already closed");
    }

    @Override
    public void close() throws Exception {
        if (this.input!=null) {
            this.input.close();
        }
        this.input = null;
        this.value = null;
    }

    //TODO: Equals Hascode
}
