package com.dcrux.haufen.kugel;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 14/09/14.
 */
public interface IKugel {
    void setHaufen(IHaufen haufen);

    ICheck create(KugelType type);

    <T extends ICheck> T create(KugelTypes<T> type);

    ICheck getWrapper(IElement elementConfiguration);

    long getComplexity(ICheck check);

    long getComplexity(IElement checkConfiguration);

    IElement getConfiguration(ICheck check);

    @Nullable
    IValidationResult validate(IElement checkConfiguration, IElement target);

    @Nullable
    IValidationResult validate(ICheck check, IElement target);
}
