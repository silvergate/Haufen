package com.dcrux.haufen.kugel.refimplementation.check.collection;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.common.IElementCollection;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ICheck;
import com.dcrux.haufen.kugel.refimplementation.ICheckFactory;
import com.dcrux.haufen.kugel.refimplementation.ISingleCheckFactory;
import com.dcrux.haufen.kugel.refimplementation.check.AbstractCheck;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * Created by caelis on 14/09/14.
 */
public class ElementCollectionCheck extends AbstractCheck<IElementCollection<IElement, ?>> {

    public static ISingleCheckFactory<ElementCollectionCheck> FACTORY = new ISingleCheckFactory<ElementCollectionCheck>() {
        @Override
        public ElementCollectionCheck create(IHaufen haufen, ICheckFactory checkFactory, IMapElement element) {
            return new ElementCollectionCheck(haufen, checkFactory, element);
        }

        @Override
        public String getTypeIdentifier() {
            return "iterator";
        }
    };

    @Nullable
    private ICheck check;
    private IElement keyChecks;
    private IElement keyLimit;
    private ICheckFactory checkFactory;

    public ElementCollectionCheck(IHaufen haufen, ICheckFactory checkFactory, IMapElement element) {
        super(haufen, element);
        this.checkFactory = checkFactory;
        this.keyChecks = haufen.create(Types.STRING).set("check");
        this.keyLimit = haufen.create(Types.STRING).set("limit");

        if (!element.exists(this.keyLimit)) {
            element.put(this.keyLimit, haufen.create(Types.INTEGER));
        }
        transferFromElementToCheck();
    }

    private void transferFromElementToCheck() {
        if (getConfig().exists(this.keyChecks)) {
            final IMapElement check = getConfig().get(this.keyChecks).as(Types.MAP);
            this.check = this.checkFactory.create(check);
        }
    }

    @Nullable
    public ICheck getCheck() {
        return this.check;
    }

    public ElementCollectionCheck setCheck(ICheck check) {
        this.check = check;
        getConfig().put(this.keyChecks, check.getConfig());
        return this;
    }

    @Nullable
    @Override
    protected Type getRequiredType() {
        return null;
    }

    @Nullable
    @Override
    protected Class<?> getRequiredClass() {
        return IElementCollection.class;
    }

    public ElementCollectionCheck setLimit(int limit) {
        if (limit < 1)
            throw new IllegalArgumentException("Limit has to be at least 1.");
        getConfig().get(this.keyLimit).as(Types.INTEGER).uset(limit);
        return this;
    }

    public int getLimit() {
        long limit = getConfig().get(this.keyLimit).as(Types.INTEGER).get();
        if (limit < 1 || limit > Integer.MAX_VALUE)
            throw new IllegalStateException("Invalid configuration. Limit has to be 1 or larger but smaller than Integer.MAX_VALUE");
        return (int) limit;
    }

    @Nullable
    private CheckValidationFailed performChecks(IElementCollection<IElement, ?> elementToCheckAgainst) {
        Iterator<IElement> iterator = elementToCheckAgainst.iterator();
        int limit = getLimit();
        int processed = 0;
        ICheck subcheck = getCheck();
        while (iterator.hasNext()) {
            if (processed >= limit) {
                return new CheckValidationFailed("The given collection is larger than the number of elements we could validate. Limit is set to " + limit + ". There are more elements in this collection.", this, (IElement) elementToCheckAgainst);
            }

            IElement element = iterator.next();
            final CheckValidationFailed failure = subcheck.check(element);
            if (failure != null)
                return failure;

            processed++;
        }
        return null;
    }

    @Override
    protected boolean isAllowNullElements() {
        return false;
    }

    @Override
    protected CheckValidationFailed checkSecondStep(IElementCollection element) {
        return performChecks(element);
    }

    @Override
    public long getComplexity() {
        /* Worst case complexity */
        return getLimit() + (getLimit() + this.check.getComplexity());
    }
}
