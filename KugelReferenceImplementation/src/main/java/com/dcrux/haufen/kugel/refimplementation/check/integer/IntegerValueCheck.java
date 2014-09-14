package com.dcrux.haufen.kugel.refimplementation.check.integer;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.kugel.refimplementation.CheckValidationFailed;
import com.dcrux.haufen.kugel.refimplementation.ISingleCheckFactory;
import com.dcrux.haufen.kugel.refimplementation.check.AbstractCheck;
import org.jetbrains.annotations.Nullable;

/**
 * Created by caelis on 13/09/14.
 */
public class IntegerValueCheck extends AbstractCheck<IIntegerElement> {

    public static ISingleCheckFactory<IntegerValueCheck> FACTORY = new ISingleCheckFactory<IntegerValueCheck>() {
        @Override
        public IntegerValueCheck create(IHaufen haufen, com.dcrux.haufen.kugel.refimplementation.ICheckFactory checkFactory, IMapElement element) {
            return new IntegerValueCheck(haufen, element);
        }

        @Override
        public String getTypeIdentifier() {
            return "int";
        }
    };

    private static final String CMP_EQUALS = "equal";
    private static final String CMP_GT = "gt";
    private static final String CMP_LE = "le";
    private IElement keyValue;
    private IElement keyComparator;

    public IntegerValueCheck(IHaufen haufen, IElement element) {
        super(haufen, (IMapElement) element);
        this.keyValue = haufen.create(Types.STRING).set("value");
        this.keyComparator = haufen.create(Types.STRING).set("comparator");

        if (!getConfig().exists(this.keyValue))
            getConfig().put(this.keyValue, getHaufen().create(Types.INTEGER));
        if (!getConfig().exists(this.keyComparator))
            getConfig().put(this.keyComparator, getHaufen().create(Types.STRING).set(CMP_EQUALS));
    }

    @Nullable
    @Override
    protected Type getRequiredType() {
        return Type.integer;
    }

    @Override
    protected CheckValidationFailed checkSecondStep(IIntegerElement element) {
        boolean fullfilled;
        switch (getComparator()) {
            case CMP_EQUALS:
                fullfilled = element.compareValueTo(getConfig().get(this.keyValue).as(Types.INTEGER)) == 0;
                break;
            case CMP_GT:
                fullfilled = element.compareValueTo(getConfig().get(this.keyValue).as(Types.INTEGER)) > 0;
                break;
            case CMP_LE:
                fullfilled = element.compareValueTo(getConfig().get(this.keyValue).as(Types.INTEGER)) < 0;
                break;
            default:
                fullfilled = false;
                break;
        }
        if (!fullfilled) {
            return new CheckValidationFailed("Integer value is invalid. Value: " + element + ", comparator " + getComparator() + " - compare to '" + getConfig().get(this.keyValue) + "',", this, element);
        }
        return null;
    }

    private long getValue() {
        return getConfig().get(this.keyValue).as(Types.INTEGER).get();
    }

    private IntegerValueCheck setValue(long value, boolean unsigned) {
        if (unsigned)
            getConfig().get(this.keyValue).as(Types.INTEGER).uset(value);
        else
            getConfig().get(this.keyValue).as(Types.INTEGER).set(value);
        return this;
    }

    private String getComparator() {
        return getConfig().get(this.keyComparator).as(Types.STRING).get();
    }

    private IntegerValueCheck setComparator(String comparator) {
        getConfig().get(this.keyComparator).as(Types.STRING).set(comparator);
        return this;
    }

    @Override
    public long getComplexity() {
        return 8;
    }


}
