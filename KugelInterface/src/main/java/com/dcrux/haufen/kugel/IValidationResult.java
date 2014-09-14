package com.dcrux.haufen.kugel;

import com.dcrux.haufen.IElement;

/**
 * Created by caelis on 14/09/14.
 */
public interface IValidationResult {
    String getMessage();

    IElement getConfiguration();

    IElement getTarget();
}
