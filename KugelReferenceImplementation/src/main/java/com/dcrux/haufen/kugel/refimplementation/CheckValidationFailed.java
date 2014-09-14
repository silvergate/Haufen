package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 13/09/14.
 */
public class CheckValidationFailed {
    private final String message;
    private final ICheck check;
    private final IElement element;

    public CheckValidationFailed(String message, ICheck check, IElement element) {
        this.message = message;
        this.check = check;
        this.element = element;
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

    @Override
    public String toString() {
        return "CheckValidationFailed{" +
                "message='" + message + '\'' +
                ", check=" + check +
                ", element=" + element +
                '}';
    }
}
