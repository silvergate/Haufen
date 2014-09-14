package com.dcrux.haufen.kugel.refimplementation;

import com.dcrux.haufen.IElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 13/09/14.
 */
public interface ICheck {
    @Nullable
    CheckValidationFailed check(@Nullable IElement element);

    IElement getConfig();

    long getComplexity();

    void validate();
}
