package com.dcrux.haufen.newimpl.elements;

import com.dcrux.haufen.newimpl.IElement;

import java.io.IOException;

/**
 * Created by caelis on 27/08/14.
 */
public class BaseCanonicalComparatorUtil {
    public static int compareTo(IElement self, IElement other) throws IOException {
        int btCmp = Byte.compare(self.getBaseType().getCode(), other.getBaseType().getCode());
        if (btCmp!=0)
            return btCmp;
        int stCmp = Byte.compare(self.getSubtype(), other.getSubtype());
        if (stCmp!=0)
            return stCmp;
        int sizeCmp = Long.compare(self.getSizeFootprint(), other.getSizeFootprint());
        if (sizeCmp!=0)
            return sizeCmp;
        return 0;
    }
}
