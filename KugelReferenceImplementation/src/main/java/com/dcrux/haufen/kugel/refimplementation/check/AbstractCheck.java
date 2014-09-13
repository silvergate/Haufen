package com.dcrux.haufen.kugel.refimplementation.check;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ICheck;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 13/09/14.
 */
public class AbstractCheck<T extends IElement, TConfig extends IElement> implements ICheck {

    private final IHaufen haufen;
    private TConfig configElement;

    public AbstractCheck(IHaufen haufen, TConfig configElement) {
        this.haufen = haufen;
        this.configElement = configElement;
    }

    protected final IHaufen getHaufen() {
        return haufen;
    }

    @Nullable
    protected Type getRequiredType() {
        return null;
    }

    protected boolean isAllowNullElements() {
        return false;
    }

    @Nullable
    public final CheckValidationFailed check(@Nullable IElement element) {
        if (!isAllowNullElements()) {
            if (element == null)
                return new CheckValidationFailed("Element is missing (according to the validation, the element should exist).");
        }
        if (element != null && getRequiredType() != null) {
            if (!element.is(getRequiredType())) {
                return new CheckValidationFailed("Element is present but it's of wrong type according to the validation. Element is of type " + element.getType() + ", type " + getRequiredType() + " is expected.");
            }
        }
        if (element != null)
            return checkSecondStep((T) element);
        else
            return checkSecondNull();
    }

    protected CheckValidationFailed checkSecondStep(T element) {
        return null;
    }

    protected CheckValidationFailed checkSecondNull() {
        return null;
    }

    public long getComplexity() {
        return 1;
    }

    public TConfig getConfig() {
        return this.configElement;
    }

    @Override
    public String toString() {
        return "Check{" +
                configElement +
                '}';
    }
}
