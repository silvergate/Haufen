package com.dcrux.haufen.newimpl.elements.common;

import com.dcrux.haufen.impl.BinaryUtil;
import com.dcrux.haufen.impl.Varint;
import com.dcrux.haufen.impl.base.IDataInput;
import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.BoxedValue;
import com.dcrux.haufen.newimpl.utils.InverseDataInput;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by caelis on 07/09/14.
 */
public class CommonListWriter {

    private static final int ADDITIONAL_HEADER_NUM_ELEMENTS = 16;
    private static CommonListWriter instance;

    public static CommonListWriter getInstance() {
        if (instance == null)
            instance = new CommonListWriter();
        return instance;
    }

    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider, BoxedValue<Boolean> outWithAdditionalHeader, Iterator<IElement> elements) {
        final int[] numberOfElements = {0};
        elements.forEachRemaining(element -> {
            numberOfElements[0]++;
            final int elementNumber = elementIndexProvider.getIndex(element);
            Varint.writeUnsignedVarInt(elementNumber, output);
        });
        if (hasAdditionalHeader(numberOfElements[0])) {
            output.write(BinaryUtil.reverseCopy(Varint.writeUnsignedVarInt(numberOfElements[0])));
            outWithAdditionalHeader.setValue(true);
        } else {
            outWithAdditionalHeader.setValue(false);
        }
    }

    public Iterator<IElement> read(IDataInput dataInput, final boolean withAdditionalHeader, IElementProvider elementProvider) {
        dataInput.seek(0);
        final int lastElement;
        if (withAdditionalHeader) {
            final InverseDataInput inverseDataInput = new InverseDataInput(dataInput);
            inverseDataInput.seek(0);
            Varint.readUnsignedVarInt(inverseDataInput);
            lastElement = (int) ((int) (dataInput.getLength() - 1) - inverseDataInput.getPosition());
            inverseDataInput.release();
        } else {
            lastElement = (int) (dataInput.getLength() - 1);
        }
        return new Iterator<IElement>() {
            @Override
            public boolean hasNext() {
                return (dataInput.getPosition() < lastElement);
            }

            @Override
            public IElement next() {
                final int elementNumber = Varint.readUnsignedVarInt(dataInput);
                return elementProvider.getElement(elementNumber);
            }
        };
    }

    public void addToList(Collection<IElement> list, Iterator<IElement> iterator) {
        iterator.forEachRemaining(list::add);
    }

    public boolean hasAdditionalHeader(int numberOfElements) {
        return numberOfElements >= ADDITIONAL_HEADER_NUM_ELEMENTS;
    }
}
