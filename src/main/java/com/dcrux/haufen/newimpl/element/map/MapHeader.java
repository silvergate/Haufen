package com.dcrux.haufen.newimpl.element.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caelis on 04/09/14.
 */
public class MapHeader {
    private Map<Integer, Integer> map = new LinkedHashMap<>();

    public Integer getNumberOfElements() {
        return this.map.size();
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }
}
