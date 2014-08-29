package com.dcrux.haufen.impl.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by caelis on 10/08/14.
 */
public class DataInputImpl implements IDataInput {
    private final byte[] data;
    private final InputStream inputStream;
    private long pointer;

    public DataInputImpl(byte[] data) {
        this.data = data;
        this.inputStream = new ByteArrayInputStream(data);
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        int actuallyRead = this.inputStream.read(b);
        if (actuallyRead != b.length)
            throw new IOException("Not enough data available");
        this.pointer += b.length;
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        int actuallyRead = this.inputStream.read(b, off, len);
        if (actuallyRead != len)
            throw new IOException("Not enough data available");
        this.pointer += len;
    }

    @Override
    public byte readByte() throws IOException {
        byte data = (byte) this.inputStream.read();
        this.pointer++;
        return data;
    }

    @Override
    public long getPosition() {
        return this.pointer;
    }

    @Override
    public long getLength() throws IOException {
        return this.data.length;
    }

    @Override
    public void seek(long position) throws IOException {
        this.inputStream.reset();
        long skipped = this.inputStream.skip(position);
        if (skipped != position)
            throw new IOException("Could not seek. Seeked to position " + skipped + " only.");
    }
}
