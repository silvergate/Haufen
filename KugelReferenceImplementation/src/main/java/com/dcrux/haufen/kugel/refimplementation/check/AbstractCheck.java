package com.dcrux.haufen.kugel.refimplementation.check;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ICheck;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 13/09/14.
 */
public class AbstractCheck<T> implements ICheck {

    private final IHaufen haufen;
    private IMapElement configElement;

    public AbstractCheck(IHaufen haufen, IMapElement configElement) {
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
    protected Class<?> getRequiredClass() {
        return null;
    }

    @Nullable
    public final CheckValidationFailed check(@Nullable IElement element) {
        if (!isAllowNullElements()) {
            if (element == null)
                return new CheckValidationFailed("Element is missing (according to the validation, the element should exist).", this, element);
        }
        if (element != null && getRequiredType() != null) {
            if (!element.is(getRequiredType())) {
                return new CheckValidationFailed("Element is present but it's of wrong type according to the validation. Element is of type " + element.getType() + ", type " + getRequiredType() + " is expected.", this, element);
            }
        }
        if (element != null && getRequiredClass() != null) {
            if (!getRequiredClass().isAssignableFrom(element.getClass())) {
                return new CheckValidationFailed("The element is not of type '" + getRequiredClass() + "'. This check cannot validate against given element " + element.getClass() + "'.", this, element);
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

    public IMapElement getConfig() {
        return this.configElement;
    }

    @Override
    public String toString() {
        return "Check{" +
                configElement +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractCheck that = (AbstractCheck) o;

        if (!configElement.equals(that.configElement)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return configElement.hashCode();
    }

    @Override
    public void validate() {

    }
}
