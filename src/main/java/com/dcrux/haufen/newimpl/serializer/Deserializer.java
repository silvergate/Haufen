package com.dcrux.haufen.newimpl.serializer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.newimpl.IElementFactory;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.index.IndexElement;
import com.dcrux.haufen.newimpl.element.index.IndexEntry;
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
    private Map<Integer, IInternalElement> elementMap = new HashMap<>();

    private IElementProvider elementProvider = elementNumber -> {
        IInternalElement element = elementMap.get(elementNumber);
        if (element == null) {
            element = loadToMap(elementNumber);
        }
        return element;
    };

    private IInternalElement loadToMap(int index) {
        IndexEntry indexEntry = this.indexElement.getEntries().get(index);
        IElementFactory elementFactory = factoryProvider.get(indexEntry.getType());
        SubInput subinput = new SubInput(this.dataInput, indexEntry.getStartIndex(), indexEntry.getStartIndex() + indexEntry.getLength() - 1);
        subinput.seek(0);
        IInternalElement element = elementFactory.create(subinput, indexEntry.getSubtype(), this.elementProvider);
        subinput.release();
        this.elementMap.put(index, element);
        return element;
    }

    public IInternalElement deserialize(IDataInput dataInput) {
        assert (this.indexElement == null);
        this.dataInput = dataInput;
        this.indexElement = (IndexElement) factoryProvider.get(Type.index).create(dataInput, (byte) 0, this.elementProvider);

        return loadToMap(0);
    }
}
