package com.dcrux.haufen.newimpl.base;

import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.element.DataException;

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
    private int retainCount;

    public DataInputImpl(byte[] data) {
        this.data = data;
        this.inputStream = new ByteArrayInputStream(data);
        retain();
    }

    @Override
    public void readFully(byte[] b) {

        try {
            int actuallyRead = this.inputStream.read(b);

            if (actuallyRead != b.length)
                throw new DataException("Not enough data available");
            this.pointer += b.length;
        } catch (IOException e) {
            throw new DataException("Error reading", e);
        }
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        try {
            int actuallyRead = this.inputStream.read(b, off, len);
            if (actuallyRead != len)
                throw new IOException("Not enough data available");
            this.pointer += len;
        } catch (IOException ioe) {
            throw new DataException("Error reading", ioe);
        }
    }

    @Override
    public byte readByte() {
        try {
            byte data = (byte) this.inputStream.read();
            this.pointer++;
            return data;
        } catch (IOException ioe) {
            throw new DataException("Error reading", ioe);
        }
    }

    @Override
    public long getPosition() {
        return this.pointer;
    }

    @Override
    public long getLength() {
        return this.data.length;
    }

    @Override
    public void seek(long position) {
        try {
            this.inputStream.reset();
            long skipped = this.inputStream.skip(position);
            if (skipped != position)
                throw new IOException("Could not seek. Seeked to position " + skipped + " only. Requested: " + position);
            this.pointer = position;
        } catch (IOException ioe) {
            throw new DataException("Error reading", ioe);
        }
    }

    @Override
    public void retain() {
        this.retainCount++;
    }

    @Override
    public void release() {
        this.retainCount--;
        if (this.retainCount == 0) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
                throw new DataException("Error closing input stream", e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        while (this.retainCount > 0) {
            release();
        }
    }
}
