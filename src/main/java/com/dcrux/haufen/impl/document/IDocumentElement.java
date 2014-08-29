package com.dcrux.haufen.impl.document;

import com.dcrux.haufen.iface.elements.IElement;
import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.Comparator;
import java.util.Set;

/**
 * Created by caelis on 10/08/14.
 */
public interface IDocumentElement extends IElement {
    BaseType getBaseType();

    byte getSubtype();

    void write(IReferenceMap referenceMap, IStreamSerializer serializer) throws Exception;

    Set<IDocumentElement> getDependencies();

    Comparator<IDocumentElement> getCanonicalComparator();
}
