package com.dcrux.haufen;

import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.element.annotation.IAnnotationElement;
import com.dcrux.haufen.element.bag.IBagElement;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.string.IStringElement;
import com.dcrux.haufen.newimpl.IInternalElement;
import com.dcrux.haufen.newimpl.base.DataInputImpl;
import com.dcrux.haufen.newimpl.base.DataOutputImpl;
import com.dcrux.haufen.newimpl.element.annotated.AnnotatedElement;
import com.dcrux.haufen.newimpl.element.annotation.AnnotationElement;
import com.dcrux.haufen.newimpl.element.bag.BagElement;
import com.dcrux.haufen.newimpl.element.bool.BoolElement;
import com.dcrux.haufen.newimpl.element.integer.IntegerElement;
import com.dcrux.haufen.newimpl.element.map.MapElement;
import com.dcrux.haufen.newimpl.element.string.StringElement;
import com.dcrux.haufen.newimpl.serializer.Deserializer;
import com.dcrux.haufen.newimpl.serializer.Haufen;
import com.dcrux.haufen.newimpl.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by caelis on 22/07/14.
 */
public class Test2 {


    public static void main(String[] fsd) throws Exception {
        IHaufen haufen = new Haufen();

        /* Serialize */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputImpl dataOutput = new DataOutputImpl(baos);

        /* Document */
        IStringElement str1 = haufen.create(Types.STRING).set("ABC_Dies_Ist_Ein_Text_BCA");
        IIntegerElement i1 = haufen.create(Types.INTEGER).set(43);
        IIntegerElement i2 = haufen.create(Types.INTEGER).set(43);
        IIntegerElement i3 = haufen.create(Types.INTEGER).set(43);

        IMapElement me1 = haufen.create(Types.MAP)
            .put(haufen.create(Types.STRING).set("age"), haufen.create(Types.INTEGER).set((21)))
            .put(haufen.create(Types.STRING).set("firstName"), haufen.create(Types.STRING).set("Simon"))
            .put(haufen.create(Types.STRING).set("male"), haufen.create(Types.BOOL).set(true));

        IAnnotationElement annotation1 = haufen.create(Types.ANNOTATION).set("Hallo".getBytes());
        IAnnotatedElement annotatedString = haufen.create(Types.ANNOTATED).set(haufen.create(Types.STRING).set("I'm Annotated")).annotate(annotation1);
        me1.put(new StringElement("valueWithAnnotation"), annotatedString);

        IBagElement bagElement1 = haufen.create(Types.BAG).add(str1).add(i1).add(i2).add(i3).add(i3).add(me1);

        haufen.serialize(dataOutput, bagElement1);

        System.out.println("Length: " + baos.toByteArray().length);
        System.out.println(Arrays.toString(baos.toByteArray()));

         /* Deserialize */
        DataInputImpl dataInput = new DataInputImpl(baos.toByteArray());
        IElement element = haufen.deserialize(dataInput).get();

        System.out.println("Deserialized: '" + element + "'");

        element.as(Types.BAG).iterator().forEachRemaining(be -> {
            if (be.getElement().is(Type.map)) {
                System.out.println("The Map: " + be.getElement());
                final IElement annotated = be.getElement().as(Types.MAP).get(haufen.create(Types.STRING).set("valueWithAnnotation"));
                System.out.println("Is Annotated: " + annotated.is(Type.annotated));
                System.out.println("Is StringElement: " + annotated.is(Type.string));
                System.out.println("String value: " + annotated.as(Types.STRING).get());
                System.out.println("Annotations:");
                annotated.as(Types.ANNOTATED).annotations().forEachRemaining(anno -> System.out.println("Annotation: " + anno));
            }
        });
    }
}
