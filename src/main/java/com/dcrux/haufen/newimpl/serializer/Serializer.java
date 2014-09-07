package com.dcrux.haufen.newimpl.serializer;

import com.dcrux.haufen.impl.base.IDataOutput;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.elements.index.IndexElement;
import com.dcrux.haufen.newimpl.elements.index.IndexEntry;

import java.util.*;

/**
 * Created by caelis on 01/09/14.
 */
public class Serializer {
    public void serialize(IDataOutput dataOutput, IElement element) {
        /* Collect dependencies */
        final Set<IElement> dependencies = new HashSet<>();
        collectDependencies(element, dependencies);

        /* Canonical sort */
        final List<IElement> sortedDependencies = new ArrayList<>();
        sortedDependencies.addAll(dependencies);
        Collections.sort(sortedDependencies, (o1, o2) -> o1.canonicalCompareTo(o2));

        /* Given element is always at index 0 */
        sortedDependencies.add(0, element);

        IElementIndexProvider elementIndexProvider = element1 -> {
            int index = sortedDependencies.indexOf(element1);
            if (index == -1)
                throw new RuntimeException("Element " + element1 + " not found. Added to dependencies?");
            return index;
        };

        IElementProvider elementProvider = elementIndex -> {
            IElement elementLocal = sortedDependencies.get(elementIndex);
            if (elementLocal == null)
                throw new RuntimeException("No Element at index " + elementIndex + " found. Added to dependencies?");
            return elementLocal;
        };

        /* Write elements */
        IndexElement indexElement = new IndexElement();
        writeList(dataOutput, sortedDependencies, indexElement.getEntries(), elementIndexProvider, elementProvider);

        /* At the end, write the index */
        writeIndex(dataOutput, indexElement, elementIndexProvider, elementProvider);
    }

    private void writeList(IDataOutput dataOutput, List<IElement> elements, List<IndexEntry> entries,
                           IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        for (IElement element : elements) {
            long beginPosition = dataOutput.getPosition();
            element.write(dataOutput, elementIndexProvider, elementProvider);
            long endPosition = dataOutput.getPosition();
            final IndexEntry entry = new IndexEntry(element.getBaseType(), element.getSubtype(), beginPosition, endPosition - beginPosition);
            entries.add(entry);
        }
    }

    private void writeIndex(IDataOutput dataOutput, IndexElement indexElement, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        indexElement.write(dataOutput, elementIndexProvider, elementProvider);
    }

    private void collectDependencies(IElement element, Set<IElement> dependencies) {
        final Iterator<IElement> elements = element.getDependencies();
        while (elements.hasNext()) {
            final IElement iteratorElement = elements.next();
            dependencies.add(iteratorElement);
            collectDependencies(iteratorElement, dependencies);
        }
    }
}
