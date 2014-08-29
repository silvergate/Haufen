package com.dcrux.haufen.impl.document;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IEntry;
import com.dcrux.haufen.impl.base.IStreamDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caelis on 10/08/14.
 */
public class DocumentDeserializer {

    public IDocumentElement deserialize(IStreamDeserializer streamDeserializer) throws Exception {
        /* Read payloads */
        final List<BaseType> types = new ArrayList<>();
        final List<Object> payloads = new ArrayList<>();
        Entry entry = new Entry();
        boolean end;
        do {
            end = !streamDeserializer.readEntry(entry);
            if (!end) {
                types.add(entry.baseType);
                payloads.add(entry.payload);
            }
        } while (!end);

        /* Do first step initialization */
        final List<ISecondStageDocumentElementFactory> secondStageDocumentElementFactories = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            BaseType type = types.get(i);
            IDocumentElementFactory factory = getFactoryProvider().getFactory(type);
            ISecondStageDocumentElementFactory created = factory.create(payloads.get(i));
            secondStageDocumentElementFactories.add(created);
        }

        /* Second step initialization */
        final IInverseReferenceMap inverseMap = getReserveInverseMap(secondStageDocumentElementFactories);
        for (ISecondStageDocumentElementFactory toInitialize : secondStageDocumentElementFactories) {
            toInitialize.initialize(inverseMap);
        }

        return secondStageDocumentElementFactories.get(0);
    }

    private IInverseReferenceMap getReserveInverseMap(final List<ISecondStageDocumentElementFactory> secondStageDocumentElementFactories) {
        return reference -> {
            ISecondStageDocumentElementFactory documentElement = secondStageDocumentElementFactories.get(reference);
            if (documentElement == null)
                throw new IllegalStateException("No such reference: " + reference);
            return documentElement;
        };
    }

    public DocumentFactoryProvider getFactoryProvider() {
        return new DocumentFactoryProvider();
    }

    private class Entry implements IEntry {
        private BaseType baseType;
        private byte subtype;
        private Object payload;

        @Override
        public void setBaseType(BaseType baseType) {
            this.baseType = baseType;
        }

        @Override
        public void setSubtype(byte subtype) {
            this.subtype = subtype;
        }

        @Override
        public void setPayload(Object payload) {
            this.payload = payload;
        }
    }
}
