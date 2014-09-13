package com.dcrux.haufen;

/**
 * Created by caelis on 22/07/14.
 */
public enum Type {
    //TODO: Need 'enum' ?
    empty,
    integer,
    number,
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
    list,
    table, //TODO: MISSING
    annotation,
    annotated,
    index
}
