package com.dcrux.haufen.impl.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by caelis on 10/08/14.
 */
public class DataOutputImpl implements IDataOutput {
    private final ByteArrayOutputStream outputStream;

    private long count;

    public DataOutputImpl(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public long getPosition() {
        return count;
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.outputStream.write(b);
        this.count += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        this.count += len;
    }

    @Override
    public void writeByte(int v) throws IOException {
        this.outputStream.write(v);
        this.count++;
    }
}
