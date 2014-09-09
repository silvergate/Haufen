package com.dcrux.haufen.refimplementation.base;

import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.refimplementation.element.DataException;

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
    public void write(byte[] b) {
        try {
            this.outputStream.write(b);
        } catch (IOException e) {
            throw new DataException("Cannot write to output stream", e);
        }
        this.count += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        this.count += len;
    }

    @Override
    public void writeByte(int v) {
        this.outputStream.write(v);
        this.count++;
    }
}
