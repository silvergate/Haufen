package com.dcrux.haufen.element.annotated;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.element.annotation.IAnnotationElement;

import java.util.Iterator;

/**
 * Created by caelis on 08/09/14.
 */
public interface IAnnotatedElement extends IElement {
    IElement get();

    IAnnotatedElement set(IElement target);

    IAnnotatedElement annotate(IAnnotationElement annotation);

    boolean removeAnnotation(IAnnotationElement annotation);

    boolean hasAnnotation(IAnnotationElement annotation);

    IAnnotatedElement clearAnnotations();

    int getNumberOfAnnotations();

    Iterator<IAnnotationElement> annotations();
}
