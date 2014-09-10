package com.dcrux.haufen.refimplementation;

import com.dcrux.haufen.IElement;
import com.dcrux.haufen.IHaufen;
import com.dcrux.haufen.Type;
import com.dcrux.haufen.Types;
import com.dcrux.haufen.element.annotated.IAnnotatedElement;
import com.dcrux.haufen.element.annotation.IAnnotationElement;
import com.dcrux.haufen.element.bag.IBagElement;
import com.dcrux.haufen.element.integer.IIntegerElement;
import com.dcrux.haufen.element.map.IMapElement;
import com.dcrux.haufen.element.string.IStringElement;
import com.dcrux.haufen.refimplementation.base.DataInputImpl;
import com.dcrux.haufen.refimplementation.base.DataOutputImpl;
import com.dcrux.haufen.refimplementation.element.string.StringElement;
import com.dcrux.haufen.refimplementation.serializer.Haufen;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by caelis on 22/07/14.
 */
public class Test2 {


    public static void main(String[] fsd) throws Exception {
        System.err.println("WICHTIGES TODO: Höchste Prop beim Sortieren von elementen im Index sollte die Referenzierungshäufigkeit sein (da wenn oft referenziert, sollte es eine kleine Zahl haben!)");

        IHaufen haufen = new Haufen();

        /* Serialize */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputImpl dataOutput = new DataOutputImpl(baos);

        /* Document */
        IStringElement str1 = haufen.create(Types.STRING).set("ABC_Dies_Ist_Ein_Text_BCA");
        IIntegerElement i1 = haufen.create(Types.INTEGER).set(43);
        IIntegerElement i2 = haufen.create(Types.INTEGER).set(43);
        IIntegerElement i3 = haufen.create(Types.INTEGER).set(43);

        IMapElement me1 = haufen.create(Types.MAP);

        me1.put(haufen.create(Types.STRING).set("age"), haufen.create(Types.INTEGER).set((21)))
                .put(haufen.create(Types.STRING).set("firstName"), haufen.create(Types.STRING).set("Simon"))
                .put(haufen.create(Types.STRING).set("male"), haufen.create(Types.BOOL).set(true))
                .put(haufen.create(Types.STRING).set("myself"), me1)
                .put(haufen.create(Types.STRING).set("OftenReferenced1"), haufen.create(Types.STRING).set("referenced"))
                .put(haufen.create(Types.STRING).set("OftenReferenced2"), haufen.create(Types.STRING).set("referenced"))
                .put(haufen.create(Types.STRING).set("number"), haufen.create(Types.NUMBER).set(4, 5))
                .put(haufen.create(Types.STRING).set("numberd"), haufen.create(Types.NUMBER).setDecimal(79, 3));

                /*
        for (int i=0; i<100; i++) {
            me1.put(haufen.create(Types.STRING).set("GEN" + i), haufen.create(Types.STRING).set("DaaDaDa"));
        }*/

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
