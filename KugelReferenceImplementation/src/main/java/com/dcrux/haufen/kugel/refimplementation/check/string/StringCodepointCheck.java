package com.dcrux.haufen.kugel.refimplementation.check.string;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.string.IStringElement;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ISingleCheckFactory;
import com.dcrux.haufen.kugel.refimplementation.check.AbstractCheck;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 13/09/14.
 */
public class StringCodepointCheck extends AbstractCheck<IStringElement> {

    public static ISingleCheckFactory<StringCodepointCheck> FACTORY = new ISingleCheckFactory<StringCodepointCheck>() {
        @Override
        public StringCodepointCheck create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IMapElement element) {
            return new StringCodepointCheck(haufen, element);
        }

        @Override
        public String getTypeIdentifier() {
            return "codePoints";
        }
    };

    private static final String CMP_EQUALS = "equal";
    private static final String CMP_GT = "gt";
    private static final String CMP_LE = "le";
    private IElement keyValue;
    private IElement keyComparator;

    public StringCodepointCheck(IHaufen haufen, IElement element) {
        super(haufen, (IMapElement) element);
        this.keyValue = haufen.create(Types.STRING).set("number");
        this.keyComparator = haufen.create(Types.STRING).set("comparator");

        if (!getConfig().exists(this.keyValue))
            getConfig().put(this.keyValue, getHaufen().create(Types.INTEGER));
        if (!getConfig().exists(this.keyComparator))
            getConfig().put(this.keyComparator, getHaufen().create(Types.STRING).set(CMP_EQUALS));
    }

    @Nullable
    @Override
    protected Type getRequiredType() {
        return Type.string;
    }

    @Override
    protected CheckValidationFailed checkSecondStep(IStringElement element) {
        boolean fullfilled;
        int numberOfCodepoints = element.getNumberOfCodePoints();
        long number = getConfig().get(this.keyValue).as(Types.INTEGER).get();
        switch (getComparator()) {
            case CMP_EQUALS:
                fullfilled = numberOfCodepoints == number;
                break;
            case CMP_GT:
                fullfilled = numberOfCodepoints > number;
                break;
            case CMP_LE:
                fullfilled = numberOfCodepoints < number;
                break;
            default:
                fullfilled = false;
                break;
        }
        if (!fullfilled) {
            return new CheckValidationFailed("Number of code points is invalid. Value: " + element + ", comparator " + getComparator() + " - compare to '" + getConfig().get(this.keyValue) + "'.", this, element);
        }
        return null;
    }

    private int getNumber() {
        long number = getConfig().get(this.keyValue).as(Types.INTEGER).get();
        if (number < 0 || number > Integer.MAX_VALUE)
            throw new IllegalStateException("Value is invalid (only positive numbers smaller or equal integer max range is allowed).");
        return (int) number;
    }

    private StringCodepointCheck setNumber(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Value is invalid (only positive numbers are allowed).");
        getConfig().get(this.keyValue).as(Types.INTEGER).uset(number);
        return this;
    }

    private String getComparator() {
        return getConfig().get(this.keyComparator).as(Types.STRING).get();
    }

    private StringCodepointCheck setComparator(String comparator) {
        getConfig().get(this.keyComparator).as(Types.STRING).set(comparator);
        return this;
    }

    @Override
    public long getComplexity() {
        /* Should about equal the threshold when the length of an integer is written */
        return 16;
    }
}
