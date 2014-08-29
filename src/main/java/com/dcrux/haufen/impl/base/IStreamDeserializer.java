package com.dcrux.haufen.impl.base;

import java.util.Iterator;

/**
 * Created by caelis on 10/08/14.
 */
public interface IStreamDeserializer {
    Iterator<IEntryHeader> getNewHeaderIterator() throws Exception;
    void readEntry(int index, IEntry outEntry);
    @Deprecated
    boolean readEntry(IEntry outEntry) throws Exception;
}
