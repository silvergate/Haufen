package com.dcrux.haufen.newimpl.element.binary;

import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.base.DataInputImpl;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.utils.DataCopyUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by caelis on 01/09/14.
 */
public class BinaryElement extends BaseElement implements IInternalElement {

    private IDataInput dataInput;

    public BinaryElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        this.dataInput = dataInput;
        dataInput.retain();
    }

    public BinaryElement() {
        this.dataInput = new DataInputImpl(new byte[]{});
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        DataCopyUtil.instance().copy(this.dataInput, output);
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        return Collections.<IInternalElement>emptySet().iterator();
    }

    @Override
    public String toString() {
        return "BinaryElement{length=" +
                +getLength() +
                '}';
    }

    public long getLength() {
        return this.dataInput.getLength();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryElement that = (BinaryElement) o;
        return DataCopyUtil.instance().equals(this.dataInput, that.dataInput);
    }

    @Override
    public int hashCode() {
        return DataCopyUtil.instance().hashCode(this.dataInput);
    }

    @Override
    public long getSizeFootprint() {
        return getLength();
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final BinaryElement that = (BinaryElement) other;

        return DataCopyUtil.instance().canonicalCompare(this.dataInput, that.dataInput);
    }

    @Override
    public boolean isClosed() {
        return this.dataInput == null;
    }

    private void changeDataInput(@Nullable IDataInput newDataInput) {
        if (this.dataInput != null) {
            this.dataInput.release();
            this.dataInput = null;
        }
        this.dataInput = newDataInput;
    }

    @Override
    public void close() throws Exception {
        changeDataInput(null);
    }

    @Override
    public Type getType() {
        return Type.binary;
    }

    @Override
    public byte getSubtype() {
        return 0;
    }

    public void setData(IDataInput data) {
        assert (data != null);
        changeDataInput(data);
        data.retain();
    }

}
