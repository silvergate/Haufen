package com.dcrux.haufen.refimplementation.utils;

import com.dcrux.haufen.data.IDataInput;

/**
 * Created by caelis on 01/09/14.
 */
public class SubInput implements IDataInput {
    private final IDataInput dataInput;
    private final long startIndex;
    private final long endIndex;
    private int retainCount;
    private long position = 0;

    public SubInput(IDataInput dataInput, long startIndex, long endIndex) {
        this.dataInput = dataInput;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        retain();
    }

    @Override
    public void readFully(byte[] b) {
        assurePosition();
        this.dataInput.readFully(b);
        this.position += b.length;
    }

    private void assurePosition() {
        seek(this.position);
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        assurePosition();
        this.dataInput.readFully(b, off, len);
        this.position += len;
    }

    @Override
    public byte readByte() {
        assurePosition();
        this.position += 1;
        return this.dataInput.readByte();
    }

    @Override
    public long getPosition() {
        return this.position;
    }

    @Override
    public long getLength() {
        return (this.endIndex - this.startIndex) + 1;
    }

    @Override
    public void seek(long position) {
        this.position = position;
        this.dataInput.seek(position + this.startIndex);
    }

    @Override
    public void retain() {
        this.dataInput.retain();
        this.retainCount++;
    }

    @Override
    public void release() {
        this.dataInput.release();
        this.retainCount--;
        assert (this.retainCount >= 0);
    }

    @Override
    public void close() throws Exception {
        while (this.retainCount >= 0) {
            retain();
        }
    }
}
