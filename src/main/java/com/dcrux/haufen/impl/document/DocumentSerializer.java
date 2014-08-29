package com.dcrux.haufen.impl.document;

import com.dcrux.haufen.impl.base.IStreamSerializer;

import java.util.*;

/**
 * Created by caelis on 10/08/14.
 */
public class DocumentSerializer {

    public void serialize(IStreamSerializer streamSerializer, IDocumentElement document) throws Exception {
        Set<IDocumentElement> dependencies = new HashSet<>();
        collectDependencies(dependencies, document);
        /* Remove master element, since has to be the first element */
        dependencies.remove(document);

        /* Sort dependencies */
        List<IDocumentElement> sortedElements = new ArrayList<>();
        sortedElements.addAll(dependencies);
        Collections.sort(sortedElements, new Comparator<IDocumentElement>() {
            @Override
            public int compare(IDocumentElement o1, IDocumentElement o2) {
                return o1.getCanonicalComparator().compare(o1, o2);
            }
        });

        /* Dependencies are now sorted. Add master element on top */
        sortedElements.add(0, document);

        /* Now write every element */
        IReferenceMap referenceMap = getReferenceMap(sortedElements);
        for (IDocumentElement element : sortedElements) {
            element.write(referenceMap, streamSerializer);
        }

        /* Finish writing */
        streamSerializer.done();
    }

    private IReferenceMap getReferenceMap(final List<IDocumentElement> elementsList) {
        return element -> {
            int index = elementsList.indexOf(element);
            if (index == -1)
                throw new IllegalArgumentException("The given element was not found in the dependencies list. Element: " + element);
            return index;
        };
    }

    private void collectDependencies(Set<IDocumentElement> outDependencies, IDocumentElement element) {
        if (outDependencies.contains(element)) {
            /* We can stop here, the element has already been processed. */
            return;
        }
        /* Add myself */
        outDependencies.add(element);
        /* Add my dependencies */
        for (IDocumentElement dependency : element.getDependencies()) {
            collectDependencies(outDependencies, dependency);
        }
    }
}
