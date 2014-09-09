package com.dcrux.haufen.refimplementation.serializer;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.refimplementation.IElementFactory;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.index.IndexElement;
import com.dcrux.haufen.refimplementation.element.index.IndexEntry;
import com.dcrux.haufen.refimplementation.utils.SubInput;

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
        IInternalElement element = elementFactory.createUninitialized();
        this.elementMap.put(index, element);

        /* Initialize */
        SubInput subinput = new SubInput(this.dataInput, indexEntry.getStartIndex(), indexEntry.getStartIndex() + indexEntry.getLength() - 1);
        subinput.seek(0);
        element.initialize(subinput, indexEntry.getSubtype(), this.elementProvider);
        subinput.release();

        return element;
    }

    public IInternalElement deserialize(IDataInput dataInput) {
        assert (this.indexElement == null);
        this.dataInput = dataInput;
        this.indexElement = (IndexElement) factoryProvider.get(Type.index).createUninitialized();
        this.indexElement.initialize(dataInput, (byte) 0, this.elementProvider);

        return loadToMap(0);
    }
}
