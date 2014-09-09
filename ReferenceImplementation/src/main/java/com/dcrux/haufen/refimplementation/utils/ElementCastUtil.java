package com.dcrux.haufen.refimplementation.utils;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.refimplementation.IInternalElement;

/**
 * Created by caelis on 09/09/14.
 */
public class ElementCastUtil {
    private static ElementCastUtil instance;

    public static ElementCastUtil getInstance() {
        if (instance == null)
            instance = new ElementCastUtil();
        return instance;
    }

    public IInternalElement cast(IElement element) {
        if (element instanceof IInternalElement)
            return (IInternalElement) element;
        throw new IllegalArgumentException("Use haufen to createUninitialized instances. Wrong instance type.");
    }
}
