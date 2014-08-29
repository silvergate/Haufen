package com.dcrux.haufen.impl.types;

import com.dcrux.haufen.impl.common.BaseType;
import com.dcrux.haufen.impl.base.IStreamType;
import com.dcrux.haufen.impl.types.annotation.AnnotationType;
import com.dcrux.haufen.impl.types.bag.BagType;
import com.dcrux.haufen.impl.types.bool.BooleanType;
import com.dcrux.haufen.impl.types.index.IndexType;
import com.dcrux.haufen.impl.types.integer.IntegerType;
import com.dcrux.haufen.impl.types.map.MapType;
import com.dcrux.haufen.impl.types.string.StringType;

/**
 * Created by caelis on 10/08/14.
 */
public class TypeProvider {
    public IStreamType getType(BaseType baseType) {
        switch (baseType) {
            case index:
                return new IndexType();
            case string:
                return new StringType();
            case bag:
                return new BagType();
            case map:
                return new MapType();
            case integer:
                return new IntegerType();
            case bool:
                return new BooleanType();
            case annotation:
                return new AnnotationType();
        }
        return null;
    }
}
