package com.dcrux.haufen.kugel.refimplementation.check.logical;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.set.ISetElement;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ICheck;
import com.dcrux.haufen.kugel.refimplementation.ICheckFactory;
import com.dcrux.haufen.kugel.refimplementation.ISingleCheckFactory;
import com.dcrux.haufen.kugel.refimplementation.check.AbstractCheck;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by caelis on 14/09/14.
 */
public class CombinedCheck extends AbstractCheck<IElement> {

    public static ISingleCheckFactory<CombinedCheck> FACTORY_AND = new ISingleCheckFactory<CombinedCheck>() {
        @Override
        public CombinedCheck create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IMapElement element) {
            return new CombinedCheck(haufen, checkFactory, element, Combination.and);
        }

        @Override
        public String getTypeIdentifier() {
            return "and";
        }
    };

    public static ISingleCheckFactory<CombinedCheck> FACTORY_OR = new ISingleCheckFactory<CombinedCheck>() {
        @Override
        public CombinedCheck create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IMapElement element) {
            return new CombinedCheck(haufen, checkFactory, element, Combination.or);
        }

        @Override
        public String getTypeIdentifier() {
            return "or";
        }
    };

    private Set<ICheck> checks = new HashSet<>();
    private IElement keyChecks;
    private ICheckFactory checkFactory;
    private final Combination combination;

    public CombinedCheck(IHaufen haufen, ICheckFactory checkFactory, IMapElement element, Combination combination) {
        super(haufen, element);
        this.checkFactory = checkFactory;
        this.combination = combination;
        this.keyChecks = haufen.create(Types.STRING).set("checks");
        if (!element.exists(this.keyChecks)) {
            element.put(this.keyChecks, haufen.create(Types.SET));
        }
        transferFromElementToChecks();
    }

    private void transferFromElementToChecks() {
        final ISetElement checkElements = getConfig().get(this.keyChecks).as(Types.SET);
        checkElements.iterator().forEachRemaining(element -> {
            ICheck subcheck = this.checkFactory.create(element);
            this.checks.add(subcheck);
        });
    }

    public Iterator<ICheck> getChecks() {
        return this.checks.iterator();
    }

    public boolean removeCheck(ICheck check) {
        return this.checks.remove(check);
    }

    public boolean containtCheck(ICheck check) {
        return this.checks.contains(check);
    }

    public CombinedCheck add(ICheck check) {
        final boolean added = this.checks.add(check);
        if (added) {
            /* Also add to config */
            final ISetElement checkElements = getConfig().get(this.keyChecks).as(Types.SET);
            checkElements.add(check.getConfig());
        }
        return this;
    }

    @Nullable
    @Override
    protected Type getRequiredType() {
        return null;
    }

    @Nullable
    private CheckValidationFailed performChecks(IElement elementToCheckAgainst) {
        if (this.checks.isEmpty())
            return new CheckValidationFailed("Invalid check configuration, cannot validate. Need at least one check in the combination", this, elementToCheckAgainst);
        if (this.combination == Combination.and) {
            Iterator<ICheck> check = getChecks();
            while (check.hasNext()) {
                ICheck currentCheck = check.next();
                CheckValidationFailed failure = currentCheck.check(elementToCheckAgainst);
                if (failure != null) {
                    return failure;
                }
            }
            return null;
        } else if (this.combination == Combination.or) {
            Iterator<ICheck> check = getChecks();
            while (check.hasNext()) {
                ICheck currentCheck = check.next();
                CheckValidationFailed failure = currentCheck.check(elementToCheckAgainst);
                if (failure == null) {
                    return null;
                }
            }
            /* None matched */
            return new CheckValidationFailed("No check in the or-combination matched.", this, elementToCheckAgainst);
        } else {
            throw new IllegalStateException("Unknown combination type");
        }
    }

    @Override
    protected boolean isAllowNullElements() {
        return true;
    }

    @Override
    protected CheckValidationFailed checkSecondNull() {
        return performChecks(null);
    }

    @Override
    protected CheckValidationFailed checkSecondStep(IElement element) {
        return performChecks(element);
    }

    @Override
    public long getComplexity() {
        /* Worst case complexity is the combination of all inner-checks */
        final long[] complexity = {0};
        getChecks().forEachRemaining(element -> {
            complexity[0] += element.getComplexity();
        });
        return complexity[0];
    }
}
