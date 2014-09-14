package com.dcrux.haufen.element.common;

import java.util.Iterator;

/**
 * Created by caelis on 07/09/14.
 */
public interface ICollection<TEntry, TThis extends ICollection<TEntry, TThis>> {
    void clear();

    Iterator<TEntry> iterator();

    boolean isEmpty();

    int getNumberOfEntries();

    // Returns true if the collection has been modified
    boolean remove(TEntry element);

    TThis add(TEntry element);

    // Returns true if this collection contains the given element at least once
    boolean contains(TEntry element);

    int getCount(TEntry element);
}
