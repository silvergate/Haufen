package com.dcrux.haufen.refimplementation.element;

/**
 * Created by caelis on 31/08/14.
 */
public interface ICloseable extends AutoCloseable {
    boolean isClosed();
}
