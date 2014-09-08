package com.dcrux.haufen;

/**
 * Created by caelis on 22/07/14.
 */
public enum Type {
    integer,
    number, //TODO: Missing
    string,
    bool,
    binary,
    // (keys) ordered: false, duplicates: false
    map,
    // (keys) ordered: true, duplicates: false
    orderedMap,
    // ordered: false, duplicates: true
    bag,
    // ordered: true, duplicates: true
    orderedBag, //TODO: MISSING
    // ordered: false, duplicates: false
    set,
    // ordered: true, duplicates: false
    orderedSet,
    // ordered: true, duplicates: true
    array,
    table, //TODO: MISSING
    annotation, //TODO: MISSING
    annotated, //TODO: Missing
    index
}