package com.dcrux.haufen.kugel.refimplementation.check;

/**
 * Created by caelis on 13/09/14.
 */
public class CheckValidationFailed {
    private final String message;

    public CheckValidationFailed(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckValidationFailed that = (CheckValidationFailed) o;

        if (!message.equals(that.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
