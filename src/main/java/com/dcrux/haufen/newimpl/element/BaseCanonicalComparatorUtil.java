package com.dcrux.haufen.newimpl.element;

import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.common.BaseTypeNumbers;

/**
 * Created by caelis on 27/08/14.
 */
public class BaseCanonicalComparatorUtil {
    public static int compareTo(IInternalElement self, IInternalElement other) {
        int btCmp = Byte.compare(BaseTypeNumbers.toNumber(self.getType()), BaseTypeNumbers.toNumber(other.getType()));
        if (btCmp != 0)
            return btCmp;
        int stCmp = Byte.compare(self.getSubtype(), other.getSubtype());
        if (stCmp != 0)
            return stCmp;
        int sizeCmp = Long.compare(self.getSizeFootprint(), other.getSizeFootprint());
        if (sizeCmp != 0)
            return sizeCmp;
        return 0;
    }
}
