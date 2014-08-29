package com.dcrux.haufen.impl.document;

import com.dcrux.haufen.impl.common.BaseType;

/**
 * Created by caelis on 10/08/14.
 */
public interface IDocumentElementFactory {
    BaseType getBaseType();

    ISecondStageDocumentElementFactory create(Object payload);

}
