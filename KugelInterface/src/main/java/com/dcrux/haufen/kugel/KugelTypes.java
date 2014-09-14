package com.dcrux.haufen.kugel;

import com.dcrux.haufen.kugel.check.ICodePointCheck;

/**
 * Created by caelis on 14/09/14.
 */
public final class KugelTypes<T extends ICheck> {
    private final KugelType type;

    private KugelTypes(KugelType type) {
        this.type = type;
    }

    public static KugelTypes<ICodePointCheck> CODE_POINT = new KugelTypes<>(KugelType.codePoint);

    public KugelType getType() {
        return type;
    }
}
