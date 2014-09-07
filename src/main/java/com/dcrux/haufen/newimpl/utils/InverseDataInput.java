package com.dcrux.haufen.newimpl.utils;

import com.dcrux.haufen.impl.base.IDataInput;

/**
 * Created by caelis on 01/09/14.
 */
public class InverseDataInput implements IDataInput {
    private final IDataInput dataInput;
    private int retainCount;
    private long position;

    public InverseDataInput(IDataInput dataInput) {
        this.dataInput = dataInput;
        retain();
    }

    private void assurePosition() {
        seek(this.position);
    }

    @Override
    public void readFully(byte[] b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte readByte() {
        assurePosition();
        long beforePosition = getPosition();
        byte value = this.dataInput.readByte();
        this.position = beforePosition + 1;
        return value;
    }

    @Override
    public long getPosition() {
        return this.position;
    }

    @Override
    public long getLength() {
        return this.dataInput.getLength();
    }

    @Override
    public void seek(long position) {
        this.dataInput.seek(getLength() - position - 1);
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
    public void close() {
        while (this.retainCount >= 0) {
            retain();
        }
    }
}
