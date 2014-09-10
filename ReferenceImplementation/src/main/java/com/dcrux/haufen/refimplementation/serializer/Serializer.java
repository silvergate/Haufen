package com.dcrux.haufen.refimplementation.serializer;

import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.refimplementation.IElementIndexProvider;
import com.dcrux.haufen.refimplementation.IElementProvider;
import com.dcrux.haufen.refimplementation.IInternalElement;
import com.dcrux.haufen.refimplementation.element.index.IndexElement;
import com.dcrux.haufen.refimplementation.element.index.IndexEntry;

import java.util.*;

/**
 * Created by caelis on 01/09/14.
 */
public class Serializer {
    public void serialize(IDataOutput dataOutput, IInternalElement element) {
        /* Collect dependencies */
        final Map<IInternalElement, Integer> dependenciesToCount = new HashMap<>();
        collectDependencies(element, dependenciesToCount);

        /* Canonical sort */
        final List<IInternalElement> sortedDependencies = new ArrayList<>();
        sortedDependencies.addAll(dependenciesToCount.keySet());
        Collections.sort(sortedDependencies, (o1, o2) -> {
            int o1Count = dependenciesToCount.get(o1);
            int o2Count = dependenciesToCount.get(o2);
            /* First sort by reference count and then by canonical compare. This makes sure, that often reference items get a low element number. */
            if (o1Count != o2Count)
                return o2Count - o1Count;
            else
                return o1.canonicalCompareTo(o2);
        });

        /* Given element is always at index 0 */
        sortedDependencies.add(0, element);

        IElementIndexProvider elementIndexProvider = element1 -> {
            int index = sortedDependencies.indexOf(element1);
            if (index == -1)
                throw new RuntimeException("Element " + element1 + " not found. Added to dependencies?");
            return index;
        };

        IElementProvider elementProvider = elementIndex -> {
            IInternalElement elementLocal = sortedDependencies.get(elementIndex);
            if (elementLocal == null)
                throw new RuntimeException("No Element at index " + elementIndex + " found. Added to dependencies?");
            return elementLocal;
        };

        /* Write element */
        IndexElement indexElement = new IndexElement();
        writeList(dataOutput, sortedDependencies, indexElement.getEntries(), elementIndexProvider, elementProvider);

        /* At the end, write the index */
        writeIndex(dataOutput, indexElement, elementIndexProvider, elementProvider);
    }

    private void writeList(IDataOutput dataOutput, List<IInternalElement> elements, List<IndexEntry> entries,
                           IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        for (IInternalElement element : elements) {
            long beginPosition = dataOutput.getPosition();
            element.write(dataOutput, elementIndexProvider, elementProvider);
            long endPosition = dataOutput.getPosition();
            final IndexEntry entry = new IndexEntry(element.getType(), element.getSubtype(), beginPosition, endPosition - beginPosition);
            entries.add(entry);
        }
    }

    private void writeIndex(IDataOutput dataOutput, IndexElement indexElement, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        indexElement.write(dataOutput, elementIndexProvider, elementProvider);
    }

    private void collectDependencies(IInternalElement element, Map<IInternalElement, Integer> dependenciesToCount) {
        final Iterator<IInternalElement> elements = element.getDependencies();
        while (elements.hasNext()) {
            final IInternalElement iteratorElement = elements.next();
            final Integer currentCount = dependenciesToCount.get(iteratorElement);
            if (currentCount == null) {
                /* A new element, set count to 1 */
                dependenciesToCount.put(iteratorElement, 1);
                /* Collect reinsient dependencies */
                collectDependencies(iteratorElement, dependenciesToCount);
            } else {
                /* Already in list, increase count */
                dependenciesToCount.put(iteratorElement, currentCount + 1);
            }
        }
    }
}
