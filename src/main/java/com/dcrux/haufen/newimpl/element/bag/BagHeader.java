package com.dcrux.haufen.newimpl.element.bag;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by caelis on 04/09/14.
 */
public class BagHeader {
    private Integer numberOfElements = 0;
    private Integer numberOfUniqueElements = 0;
    private Map<Integer, BagElementGroup> elementCountToElementGroups = new TreeMap<>(Integer::compare);

    public void addElement(int elementCount, int elementNumber) {
        BagElementGroup bagElementGroup = this.elementCountToElementGroups.get(elementCount);
        if (bagElementGroup == null) {
            bagElementGroup = new BagElementGroup();
            this.elementCountToElementGroups.put(elementCount, bagElementGroup);
        }
        if (!bagElementGroup.getElementNumbers().contains(elementNumber))
            bagElementGroup.getElementNumbers().add(elementNumber);
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getNumberOfUniqueElements() {
        return numberOfUniqueElements;
    }

    public void setNumberOfUniqueElements(Integer numberOfUniqueElements) {
        this.numberOfUniqueElements = numberOfUniqueElements;
    }

    public Map<Integer, BagElementGroup> getElementCountToElementGroups() {
        return elementCountToElementGroups;
    }
}
