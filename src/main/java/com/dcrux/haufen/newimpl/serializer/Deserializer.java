package com.dcrux.haufen.newimpl.serializer;

import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.index.IndexElement;
import com.dcrux.haufen.newimpl.elements.index.IndexEntry;
import com.dcrux.haufen.newimpl.utils.SubInput;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caelis on 01/09/14.
 */
public class Deserializer {
    private final FactoryProvider factoryProvider = new FactoryProvider();

    private IDataInput dataInput;
    private IndexElement indexElement;
    private Map<Integer, IElement> elementMap = new HashMap<>();

    private IElementProvider elementProvider = elementNumber -> {
        IElement element = elementMap.get(elementNumber);
        if (element == null) {
            element = loadToMap(elementNumber);
        }
        return element;
    };

    private IElement loadToMap(int index) {
        IndexEntry indexEntry = this.indexElement.getEntries().get(index);
        IElementFactory elementFactory = factoryProvider.get(indexEntry.getBaseType());
        SubInput subinput = new SubInput(this.dataInput, indexEntry.getStartIndex(), indexEntry.getStartIndex() + indexEntry.getLength() - 1);
        subinput.seek(0);
        IElement element = elementFactory.create(subinput, indexEntry.getSubtype(), this.elementProvider);
        subinput.release();
        this.elementMap.put(index, element);
        return element;
    }

    public IElement deserialize(IDataInput dataInput) {
        assert (this.indexElement == null);
        this.dataInput = dataInput;
        this.indexElement = (IndexElement) factoryProvider.get(BaseType.index).create(dataInput, (byte) 0, this.elementProvider);

        return loadToMap(0);
    }
}
