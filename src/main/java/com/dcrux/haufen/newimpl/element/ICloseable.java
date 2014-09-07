package com.dcrux.haufen.newimpl.element;

/**
 * Created by caelis on 31/08/14.
 */
public interface ICloseable extends AutoCloseable {
    boolean isClosed();
}
