package com.dcrux.haufen.newimpl.element.annotated;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.data.IDataInput;
import com.dcrux.haufen.data.IDataOutput;
import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.element.annotation.IAnnotationElement;
import com.dcrux.haufen.newimpl.IElementIndexProvider;
import com.dcrux.haufen.newimpl.IElementProvider;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.element.BaseCanonicalComparatorUtil;
import com.dcrux.haufen.newimpl.element.BaseElement;
import com.dcrux.haufen.newimpl.element.BoxedValue;
import com.dcrux.haufen.newimpl.element.annotation.AnnotationElement;
import com.dcrux.haufen.newimpl.element.common.CommonListWriter;
import com.dcrux.haufen.newimpl.utils.BinaryUtil;

import java.util.*;

/**
 * Created by caelis on 01/09/14.
 */
public class AnnotatedElement extends BaseElement implements IInternalElement, IAnnotatedElement {

    private Set<AnnotationElement> annotations;
    private IInternalElement target;

    public AnnotatedElement(IDataInput dataInput, byte subtype, IElementProvider elementProvider) {
        final boolean withAdditionalHeader = isWithAdditionalHeader(subtype);
        final Iterator<IInternalElement> iterator = CommonListWriter.getInstance().read(dataInput, withAdditionalHeader, elementProvider);

        this.annotations = new HashSet<>();
        final boolean[] first = {true};
        iterator.forEachRemaining(element -> {
            if (first[0]) {
                this.target = element;
                first[0] = false;
            } else {
                if (!(element instanceof AnnotationElement)) {
                    throw new IllegalArgumentException(element + " is not of type " + AnnotationElement.class);
                }
                this.annotations.add((AnnotationElement) element);
            }
        });
    }

    public AnnotatedElement() {
        this.annotations = new HashSet<>();
    }

    private boolean isWithAdditionalHeader(byte subtype) {
        switch (subtype) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Unknown subtype");
        }
    }

    @Override
    public void write(IDataOutput output, IElementIndexProvider elementIndexProvider, IElementProvider elementProvider) {
        final List<IInternalElement> finalList = new ArrayList<>();
        finalList.addAll(this.annotations);
        finalList.sort((o1, o2) -> o1.canonicalCompareTo(o2));
        finalList.add(0, this.target);

        CommonListWriter.getInstance().write(output, elementIndexProvider, elementProvider, new BoxedValue<>(), finalList.iterator());
    }

    @Override
    public Iterator<IInternalElement> getDependencies() {
        final Iterator<AnnotationElement> annotationsIterator = this.annotations.iterator();
        return new Iterator<IInternalElement>() {
            private boolean first = true;
            @Override
            public boolean hasNext() {
                if (first)
                    return true;
                else
                    return annotationsIterator.hasNext();
            }

            @Override
            public IInternalElement next() {
                if (first) {
                    first = false;
                    return AnnotatedElement.this.target;
                } else {
                    return annotationsIterator.next();
                }
            }
        };
    }

    @Override
    public String toString() {
        return "Annotated{"+
                this.target + ":" + this.annotations +
                '}';
    }

    @Override
    public int getNumberOfAnnotations() {
        return this.annotations.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedElement that = (AnnotatedElement) o;
        if (!this.target.equals(that.target))
            return false;
        return this.annotations.equals(that.annotations);
    }

    @Override
    public int hashCode() {
        //TODO: Improve
        return this.target.hashCode() + this.annotations.hashCode();
    }

    @Override
    public long getSizeFootprint() {
        return this.annotations.size() + 1;
    }

    @Override
    public int canonicalCompareTo(IInternalElement other) {
        int dif = BaseCanonicalComparatorUtil.compareTo(this, other);
        if (dif != 0)
            return dif;
        final AnnotatedElement that = (AnnotatedElement) other;

        int targetDif = this.target.canonicalCompareTo(that.target);
        if (targetDif!=0)
            return targetDif;

        final Iterator<AnnotationElement> thisAnnotationIterator = this.annotations.iterator();
        final Iterator<AnnotationElement> thatAnnotationIterator = that.annotations.iterator();
        while (thisAnnotationIterator.hasNext()) {
            final AnnotationElement thisAnnotationElement = thisAnnotationIterator.next();
            final AnnotationElement thatAnnotationElement = thatAnnotationIterator.next();
            final int annotationDif = thisAnnotationElement.canonicalCompareTo(thatAnnotationElement);
            if (annotationDif!=0)
                return annotationDif;
        }
        return 0;
    }

    @Override
    public boolean isClosed() {
        return this.annotations == null;
    }

    @Override
    public void close() throws Exception {
        this.annotations = null; this.target = null;
    }

    @Override
    public Type getType() {
        return Type.annotated;
    }

    @Override
    public byte getSubtype() {
        boolean additionalHeader = CommonListWriter.getInstance().hasAdditionalHeader(this.annotations.size() + 1);
        if (additionalHeader)
            return 1;
        else
        return 0;
    }

    @Override
    public Iterator<IAnnotationElement> annotations() {
        final Iterator<AnnotationElement> original = this.annotations.iterator();
        return new Iterator<IAnnotationElement>() {
            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public IAnnotationElement next() {
                return original.next();
            }
        };
    }

    @Override
    public IElement get() {
        return this.target;
    }

    @Override
    public IAnnotatedElement set(IElement target) {
        this.target = (IInternalElement) target;
        return this;
    }

    @Override
    public IAnnotatedElement annotate(IAnnotationElement annotation) {
        this.annotations.add((AnnotationElement) annotation);
        return this;
    }

    @Override
    public boolean removeAnnotation(IAnnotationElement annotation) {
        return this.annotations.remove(annotation);
    }

    @Override
    public boolean hasAnnotation(IAnnotationElement annotation) {
        return this.annotations.contains(annotation);
    }

    @Override
    public AnnotatedElement clearAnnotations() {
        this.annotations.clear();
        return this;
    }
}
