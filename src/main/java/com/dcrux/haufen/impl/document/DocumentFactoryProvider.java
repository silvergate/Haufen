package com.dcrux.haufen.impl.document;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.document.elements.*;

/**
 * Created by caelis on 10/08/14.
 */
public class DocumentFactoryProvider {
    public IDocumentElementFactory getFactory(BaseType baseType) {
        switch (baseType) {
            case string:
                return StringElement.getFactory();
            case bag:
                return BagElement.getFactory();
            case map:
                return MapElement.getFactory();
            case integer:
                return IntegerElement.getFactory();
            case bool:
                return BooleanElement.getFactory();
        }
        return null;
    }
}
