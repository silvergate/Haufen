package com.dcrux.haufen.impl.document.elements;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.IDocumentElementFactory;
import com.dcrux.haufen.impl.document.ISecondStageDocumentElementFactory;

/**
 * Created by caelis on 10/08/14.
 */
public class AbstractFactory implements IDocumentElementFactory {
    @Override
    public BaseType getBaseType() {
        return null;
    }

    @Override
    public ISecondStageDocumentElementFactory create(Object payload) {
        return null;
    }
}
