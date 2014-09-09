package com.dcrux.haufen.refimplementation.element;

/**
 * Created by caelis on 01/09/14.
 */
public class DataException extends RuntimeException {
    public DataException() {
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
